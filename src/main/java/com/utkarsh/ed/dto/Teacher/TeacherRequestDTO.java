package com.utkarsh.ed.dto.Teacher;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record TeacherRequestDTO(@NotNull(message = "Name is required") String name,

		@Email(message = "Email must be valid") @NotNull(message = "Email is required") String email,

		@NotNull(message = "Phone is required") String phone) {
}
