package com.utkarsh.ed.dto.ClassSession;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Schema(description = "Request body for rescheduling a class session")
public record SessionRescheduleRequestDTO(
        @Schema(
            description = "New scheduled start time (IST). Must be in the future and within 7 days of the current scheduledAt.",
            example = "2026-03-04T10:00:00")
        @NotNull(message = "New Schedule time is required") @Future(message = "Rescheduled time must be in the future")
                LocalDateTime newScheduledAt) {}
