package com.utkarsh.ed.services;

import com.utkarsh.ed.dto.ClassSession.ClassSessionFilter;
import com.utkarsh.ed.dto.ClassSession.SessionCompletionRequestDTO;
import com.utkarsh.ed.dto.ClassSession.SessionDetailResponseDTO;
import com.utkarsh.ed.exceptions.ResourceNotFoundException;
import com.utkarsh.ed.mappers.ClassSessionMapper;
import com.utkarsh.ed.models.ClassSession;
import com.utkarsh.ed.models.SessionStatus;
import com.utkarsh.ed.repositories.ClassSessionRepository;
import com.utkarsh.ed.repositories.ClassSessionSpecification;
import jakarta.transaction.Transactional;
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
                classSessionRepository.findAll(ClassSessionSpecification.filter(filter), pageable);
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
}
