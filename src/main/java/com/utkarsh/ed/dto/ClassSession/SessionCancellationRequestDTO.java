package com.utkarsh.ed.dto.ClassSession;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Request body for cancelling a class session")
public record SessionCancellationRequestDTO(
        @Schema(description = "Reason for cancellation", example = "Teacher unavailable due to public holiday")
        @NotBlank(message = "Class cancellation reason is required") String cancellationReason) {}
