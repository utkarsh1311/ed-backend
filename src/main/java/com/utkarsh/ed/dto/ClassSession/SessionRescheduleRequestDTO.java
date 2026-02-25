package com.utkarsh.ed.dto.ClassSession;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record SessionRescheduleRequestDTO(
        @NotNull(message = "New Schedule time is required") @Future(message = "Rescheduled time must be in the future")
                LocalDateTime newScheduledAt) {}
