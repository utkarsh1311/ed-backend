package com.utkarsh.ed.services;

import com.utkarsh.ed.models.ClassSchedule;
import com.utkarsh.ed.models.ClassSession;
import com.utkarsh.ed.models.ScheduleStatus;
import com.utkarsh.ed.models.SessionStatus;
import com.utkarsh.ed.repositories.ClassScheduleRepository;
import com.utkarsh.ed.repositories.ClassSessionRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SessionGenerationService {
    private static final Logger logger = LoggerFactory.getLogger(SessionGenerationService.class);

    private final ClassScheduleRepository classScheduleRepository;
    private final ClassSessionRepository classSessionRepository;

    public SessionGenerationService(
            ClassScheduleRepository classScheduleRepository, ClassSessionRepository classSessionRepository) {
        this.classScheduleRepository = classScheduleRepository;
        this.classSessionRepository = classSessionRepository;
    }

    @Transactional
    public void generateNextWeekSessions() {
        logger.info("Starting session generation for the next week.");

        List<ClassSchedule> activeSchedules = classScheduleRepository.findAllByStatus(ScheduleStatus.ACTIVE);
        List<ClassSession> sessionsToCreate = new ArrayList<>();

        for (ClassSchedule classSchedule : activeSchedules) {
            LocalDate nextOccuranceDate = LocalDate.now().with(TemporalAdjusters.next(classSchedule.getDayOfWeek()));

            LocalDateTime scheduledAt = LocalDateTime.of(nextOccuranceDate, classSchedule.getStartTime());

            boolean alreadyGenerated =
                    classSessionRepository.existsByClassScheduleAndScheduledAt(classSchedule, scheduledAt);

            if (!alreadyGenerated) {
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

                sessionsToCreate.add(classSession);
            }
        }

        if (!sessionsToCreate.isEmpty()) {
            classSessionRepository.saveAll(sessionsToCreate);
            logger.info("Successfully generated {} class sessions.", sessionsToCreate.size());
        } else {
            logger.info("No new ClassSessions required generation. System is in sync.");
        }
    }
}
