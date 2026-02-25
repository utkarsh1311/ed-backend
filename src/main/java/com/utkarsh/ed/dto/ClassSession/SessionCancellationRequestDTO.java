package com.utkarsh.ed.dto.ClassSession;

import jakarta.validation.constraints.NotBlank;

public record SessionCancellationRequestDTO(
        @NotBlank(message = "Class cancellation reason is required") String cancellationReason) {}
