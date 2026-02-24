package com.utkarsh.ed.dto.Subject;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SubjectRequestDTO(
		@NotBlank(message = "Subject name is required") String name,
		@NotNull(message = "isSpecial flag is required") Boolean isSpecial) {}
