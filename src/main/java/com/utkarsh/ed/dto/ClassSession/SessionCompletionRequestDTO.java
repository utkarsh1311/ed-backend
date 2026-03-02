package com.utkarsh.ed.dto.ClassSession;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Schema(description = "Request body for marking a class session as completed")
public record SessionCompletionRequestDTO(
        @Schema(
                        description = "Teacher's notes or feedback about the session",
                        example = "Covered quadratic equations. Student showed good understanding.")
                @NotNull(message = "Feedback is required")
                String feedbackText,
        @Schema(description = "Actual start time of the session (IST)", example = "2026-03-02T14:05:00")
                @NotNull(message = "Class Session Start Time is required")
                LocalDateTime actualStartAt,
        @Schema(description = "Actual end time of the session (IST)", example = "2026-03-02T15:00:00")
                @NotNull(message = "Class Session End Time is required")
                LocalDateTime actualEndAt,
        @Schema(description = "Whether this session was a test / exam", example = "false") boolean isTest,
        @Schema(description = "Score achieved if isTest is true (0–100). Required when isTest=true.", example = "87.5")
                Double testScore) {}
