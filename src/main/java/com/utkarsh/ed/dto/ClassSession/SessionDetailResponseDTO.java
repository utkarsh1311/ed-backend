package com.utkarsh.ed.dto.ClassSession;

import com.utkarsh.ed.models.SessionStatus;
import java.time.LocalDateTime;

public record SessionDetailResponseDTO(
        Long id,
        String teacherName,
        String studentName,
        String subjectName,
        LocalDateTime scheduledAt,
        /** The slot this session was rescheduled FROM. null means it was never rescheduled. */
        LocalDateTime originalScheduledAt,
        LocalDateTime actualStartAt,
        LocalDateTime actualEndAt,
        SessionStatus status,
        String feedbackText,
        String cancellationReason,
        boolean isTest,
        Double testScore) {}
