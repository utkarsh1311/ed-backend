package com.utkarsh.ed.dto.ClassSession;

import com.utkarsh.ed.models.SessionStatus;
import java.time.LocalDateTime;

public record SessionSummaryResponseDTO(
        Long id,
        Long classScheduleId, // Helps frontend link back to the recurring rule
        String teacherName, // Flattened from the Teacher entity
        String studentName, // Flattened from ClassSchedule -> Student
        String subjectName, // Flattened from ClassSchedule -> Subject
        LocalDateTime scheduledAt,
        /** The slot this session was rescheduled FROM. null means never rescheduled. */
        LocalDateTime originalScheduledAt,
        SessionStatus status,
        boolean isTest) {}
