package com.utkarsh.ed.services;

import com.utkarsh.ed.dto.ClassSession.*;
import com.utkarsh.ed.exceptions.BusinessRuleException;
import com.utkarsh.ed.exceptions.ResourceNotFoundException;
import com.utkarsh.ed.mappers.ClassSessionMapper;
import com.utkarsh.ed.models.ClassSession;
import com.utkarsh.ed.models.SessionStatus;
import com.utkarsh.ed.repositories.ClassSessionRepository;
import com.utkarsh.ed.repositories.ClassSessionSpecification;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ClassSessionService {
    private static final Logger logger = LoggerFactory.getLogger(ClassSessionService.class);

    private final ClassSessionRepository classSessionRepository;
    private final ClassSessionMapper classSessionMapper;

    public ClassSessionService(ClassSessionRepository classSessionRepository, ClassSessionMapper classSessionMapper) {
        this.classSessionRepository = classSessionRepository;
        this.classSessionMapper = classSessionMapper;
    }

    public Page<SessionDetailResponseDTO> getAllSessions(ClassSessionFilter filter, Pageable pageable) {
        Page<ClassSession> classSessions =
                classSessionRepository.findAll(ClassSessionSpecification.build(filter), pageable);
        return classSessions.map(classSessionMapper::toDetailDTO);
    }

    @Transactional
    public SessionDetailResponseDTO completeSession(
            Long sessionId, Long teacherId, SessionCompletionRequestDTO sessionCompletionRequestDTO) {
        ClassSession classSession = classSessionRepository
                .findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("ClassSession with ID: " + sessionId + " not found"));

        if (!Objects.equals(teacherId, classSession.getTeacher().getId())) {
            throw new SecurityException("Unauthorized: You are not the assigned teacher for this session.");
        }

        if (classSession.getStatus() == SessionStatus.CANCELLED) {
            throw new IllegalStateException("Cannot complete a session that has been cancelled.");
        }

        if (classSession.getStatus() == SessionStatus.COMPLETED) {
            return classSessionMapper.toDetailDTO(classSession);
        }

        // A RESCHEDULED session is still completable — it just has a different slot.
        // SCHEDULED and RESCHEDULED are both valid pre-completion states.

        if (sessionCompletionRequestDTO.actualEndAt().isBefore(sessionCompletionRequestDTO.actualStartAt())) {
            throw new IllegalArgumentException("Actual end time cannot be before start time.");
        }

        classSession.setActualStartAt(sessionCompletionRequestDTO.actualStartAt());
        classSession.setActualEndAt(sessionCompletionRequestDTO.actualEndAt());
        classSession.setStatus(SessionStatus.COMPLETED);
        classSession.setFeedbackText(sessionCompletionRequestDTO.feedbackText());
        classSession.setTest(sessionCompletionRequestDTO.isTest());

        if (sessionCompletionRequestDTO.isTest()) {
            if (sessionCompletionRequestDTO.testScore() == null) {
                throw new IllegalArgumentException("Test Score is required if class is marked as Test");
            }

            classSession.setTestScore(sessionCompletionRequestDTO.testScore());
        }

        classSessionRepository.save(classSession);
        return classSessionMapper.toDetailDTO(classSession);
    }


    @Transactional
    public SessionDetailResponseDTO cancelSession(
            Long sessionId, SessionCancellationRequestDTO sessionCancellationRequestDTO) {
        ClassSession classSession = classSessionRepository
                .findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("ClassSession with ID: " + sessionId + " not found"));

        if (classSession.getStatus() == SessionStatus.CANCELLED) {
            throw new IllegalStateException("Session is already cancelled.");
        }
        if (classSession.getStatus() == SessionStatus.COMPLETED) {
            throw new IllegalStateException("Cannot cancel a session that is already completed.");
        }

        classSession.setStatus(SessionStatus.CANCELLED);
        classSession.setCancellationReason(sessionCancellationRequestDTO.cancellationReason());
        classSessionRepository.save(classSession);
        return classSessionMapper.toDetailDTO(classSession);
    }

    @Transactional
    public SessionDetailResponseDTO rescheduleSession(Long sessionId, SessionRescheduleRequestDTO dto) {
        ClassSession session = classSessionRepository
                .findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("ClassSession with ID: " + sessionId + " not found"));

        // ── 1. Status guard ───────────────────────────────────────────────────────
        if (session.getStatus() == SessionStatus.CANCELLED) {
            throw new IllegalStateException("Cannot reschedule a cancelled session.");
        }
        if (session.getStatus() == SessionStatus.COMPLETED) {
            throw new IllegalStateException("Cannot reschedule a completed session.");
        }

        LocalDateTime currentSlot = session.getScheduledAt();
        LocalDateTime newSlot = dto.newScheduledAt();

        // ── 2. Same-slot guard ────────────────────────────────────────────────────
        if (newSlot.equals(currentSlot)) {
            throw new BusinessRuleException("New scheduled time is the same as the current slot.");
        }

        // ── 3. Within-week guard ──────────────────────────────────────────────────
        // A session may only be moved within its own occurrence window.
        // The next occurrence of any session is exactly 7 days after the current slot;
        // rescheduling past that would collide with the auto-generated next session.
        LocalDateTime weekDeadline = currentSlot.plusDays(7);
        if (!newSlot.isBefore(weekDeadline)) {
            throw new BusinessRuleException(
                    "Session can only be rescheduled within the same week. New time must be before "
                            + weekDeadline + ".");
        }

        // ── 4. Conflict detection ─────────────────────────────────────────────────
        // Excluded statuses: CANCELLED sessions don't occupy a slot.
        List<SessionStatus> ignoredStatuses = List.of(SessionStatus.CANCELLED);
        int durationMinutes = session.getClassSchedule().getDurationMinutes();
        LocalDateTime newSlotEnd = newSlot.plusMinutes(durationMinutes);

        Long teacherId = session.getTeacher().getId();
        if (classSessionRepository.existsConflictForTeacher(
                session.getId(), teacherId, ignoredStatuses, newSlot, newSlotEnd)) {
            throw new BusinessRuleException(
                    "Teacher already has another session scheduled during the requested time slot.");
        }

        Long studentId = session.getClassSchedule().getStudent().getId();
        if (classSessionRepository.existsConflictForStudent(
                session.getId(), studentId, ignoredStatuses, newSlot, newSlotEnd)) {
            throw new BusinessRuleException(
                    "Student already has another session scheduled during the requested time slot.");
        }

        // ── 5. Apply reschedule ───────────────────────────────────────────────────
        // Always overwrite so originalScheduledAt reflects the immediately-previous slot.
        session.setOriginalScheduledAt(currentSlot);
        session.setScheduledAt(newSlot);
        session.setStatus(SessionStatus.RESCHEDULED);

        classSessionRepository.save(session);
        logger.info("Session {} rescheduled from {} to {}", sessionId, currentSlot, newSlot);
        return classSessionMapper.toDetailDTO(session);
    }



}
