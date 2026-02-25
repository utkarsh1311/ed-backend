package com.utkarsh.ed.dto.ClassSession;

import com.utkarsh.ed.models.SessionStatus;
import java.time.LocalDateTime;

public record SessionDetailResponseDTO(
        Long id,
        String teacherName,
        String studentName,
        String subjectName,
        LocalDateTime scheduledAt,
        LocalDateTime actualStartAt,
        LocalDateTime actualEndAt,
        SessionStatus status,
        String feedbackText,
        String cancellationReason,
        boolean isTest,
        Double testScore) {}
