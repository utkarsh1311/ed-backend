package com.utkarsh.ed.dto.ClassSession;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public record SessionCompletionRequestDTO(
        @NotBlank(message = "Feedback is required") String feedbackText,
        @NotBlank(message = "Class Session Start Time is required") LocalDateTime actualStartAt,
        @NotBlank(message = "Class Session End time is required") LocalDateTime actualEndAt,
        boolean isTest,
        Double testScore) {}
