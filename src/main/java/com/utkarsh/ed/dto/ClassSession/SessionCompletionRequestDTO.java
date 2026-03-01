package com.utkarsh.ed.dto.ClassSession;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record SessionCompletionRequestDTO(
        @NotNull(message = "Feedback is required") String feedbackText,
        @NotNull(message = "Class Session Start Time is required") LocalDateTime actualStartAt,
        @NotNull(message = "Class Session End Time is required") LocalDateTime actualEndAt,
        boolean isTest,
        Double testScore) {}
