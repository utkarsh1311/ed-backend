package com.utkarsh.ed.services;

import com.utkarsh.ed.dto.ClassSchedule.ClassScheduleFilter;
import com.utkarsh.ed.dto.ClassSchedule.ClassScheduleRequestDTO;
import com.utkarsh.ed.dto.ClassSchedule.ClassScheduleResponseDTO;
import com.utkarsh.ed.exceptions.BusinessRuleException;
import com.utkarsh.ed.exceptions.ResourceNotFoundException;
import com.utkarsh.ed.mappers.ClassScheduleMapper;
import com.utkarsh.ed.models.ClassSchedule;
import com.utkarsh.ed.models.ClassSession;
import com.utkarsh.ed.models.SessionStatus;
import com.utkarsh.ed.repositories.ClassScheduleRepository;
import com.utkarsh.ed.repositories.ClassScheduleSpecification;
import com.utkarsh.ed.repositories.ClassSessionRepository;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ClassScheduleService {

    private static final Logger logger = LoggerFactory.getLogger(ClassScheduleService.class);

    private final ClassScheduleRepository classScheduleRepository;
    private final ClassSessionRepository classSessionRepository;
    private final ClassScheduleMapper classScheduleMapper;

    public ClassScheduleService(
            ClassScheduleRepository classScheduleRepository,
            ClassSessionRepository classSessionRepository,
            ClassScheduleMapper classScheduleMapper) {
        this.classScheduleRepository = classScheduleRepository;
        this.classSessionRepository = classSessionRepository;
        this.classScheduleMapper = classScheduleMapper;
    }

    private void validateConflict(
            Long teacherId, Long studentId, DayOfWeek day, LocalTime start, LocalTime end, Long excludeId) {
        boolean hasConflict =
                classScheduleRepository.existsOverlappingSchedule(teacherId, studentId, day, start, end, excludeId);

        if (hasConflict) {
            logger.warn("Schedule conflict detected for Teacher {} or Student {} on {}", teacherId, studentId, day);
            throw new BusinessRuleException("The teacher or student already has a class scheduled during this time.");
        }
    }

    private void createInitialClassSessions(ClassSchedule classSchedule) {
        logger.info("Creating Initial class for ClassSchedule with ID: {}", classSchedule.getId());
        LocalDate nextOccuranceDate = LocalDate.now().with(TemporalAdjusters.next(classSchedule.getDayOfWeek()));
        LocalDateTime scheduledAt = LocalDateTime.of(nextOccuranceDate, classSchedule.getStartTime());

        ClassSession classSession = new ClassSession(
                classSchedule,
                classSchedule.getTeacher(),
                scheduledAt,
                null,
                null,
                SessionStatus.SCHEDULED,
                null,
                null,
                false,
                null);

        classSessionRepository.save(classSession);

        logger.info("Initial class session generated for ClassSchedule with ID: {}", classSchedule.getId());
    }

    // Create Class Schedule
    public ClassScheduleResponseDTO createClassSchedule(ClassScheduleRequestDTO scheduleRequestDTO) {

        LocalTime calculateEndTime = scheduleRequestDTO.startTime().plusMinutes(scheduleRequestDTO.durationMinutes());

        validateConflict(
                scheduleRequestDTO.teacherId(),
                scheduleRequestDTO.studentId(),
                scheduleRequestDTO.dayOfWeek(),
                scheduleRequestDTO.startTime(),
                calculateEndTime,
                -1L);

        ClassSchedule classSchedule = classScheduleMapper.toEntity(scheduleRequestDTO);
        classSchedule.setEndTime(calculateEndTime);

        ClassSchedule savedSchedule = classScheduleRepository.save(classSchedule);
        createInitialClassSessions(savedSchedule);
        logger.info("ClassSchedule created with ID: {}", savedSchedule.getId());

        return classScheduleMapper.toResponse(savedSchedule);
    }

    // Get all classSchedules with optional filters
    public Page<ClassScheduleResponseDTO> getAllClassSchedules(ClassScheduleFilter filter, Pageable pageable) {
        Page<ClassSchedule> classSchedules =
                classScheduleRepository.findAll(ClassScheduleSpecification.build(filter), pageable);
        return classSchedules.map(classScheduleMapper::toResponse);
    }

    // get a class Schedule
    public ClassScheduleResponseDTO getClassScheduleByID(Long id) {
        ClassSchedule classSchedule = classScheduleRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Class Schedule not found with ID: " + id));
        return classScheduleMapper.toResponse(classSchedule);
    }

    // update class Schedule
    public ClassScheduleResponseDTO updateClassSchedule(Long id, ClassScheduleRequestDTO classScheduleRequestDTO) {

        ClassSchedule exisitingClassSchedule = classScheduleRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ClassSchedule not found with ID: " + id));

        boolean requiresConflictCheck = classScheduleRequestDTO.teacherId() != null
                || classScheduleRequestDTO.studentId() != null
                || classScheduleRequestDTO.dayOfWeek() != null
                || classScheduleRequestDTO.startTime() != null
                || classScheduleRequestDTO.durationMinutes() != null;

        if (requiresConflictCheck) {

            Long teacherId = Objects.requireNonNullElse(
                    classScheduleRequestDTO.teacherId(),
                    exisitingClassSchedule.getTeacher().getId());
            Long studentId = Objects.requireNonNullElse(
                    classScheduleRequestDTO.studentId(),
                    exisitingClassSchedule.getStudent().getId());
            DayOfWeek day = Objects.requireNonNullElse(
                    classScheduleRequestDTO.dayOfWeek(), exisitingClassSchedule.getDayOfWeek());
            LocalTime start = Objects.requireNonNullElse(
                    classScheduleRequestDTO.startTime(), exisitingClassSchedule.getStartTime());
            int duration = Objects.requireNonNullElse(
                    classScheduleRequestDTO.durationMinutes(), exisitingClassSchedule.getDurationMinutes());

            validateConflict(teacherId, studentId, day, start, start.plusMinutes(duration), id);
        }

        classScheduleMapper.updateFromDto(classScheduleRequestDTO, exisitingClassSchedule);
        ClassSchedule classSchedule = classScheduleRepository.save(exisitingClassSchedule);
        return classScheduleMapper.toResponse(classSchedule);
    }

    // delete a class Schedule
    public void deleteClassSchedule(Long id) {
        if (!classScheduleRepository.existsById(id)) {
            throw new ResourceNotFoundException("ClassSchedule with id:" + id + " doesn't exist");
        }

        classScheduleRepository.deleteById(id);
        logger.info("ClassSchedule with id {} successfully deleted", id);
    }
}
