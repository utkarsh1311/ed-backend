package com.utkarsh.ed.dto.Teacher;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record TeacherRequestDTO(
        @NotNull(message = "Name is required") String name,
        @Email(message = "Personal email must be valid") @NotNull(message = "Personal email is required")
                String personalEmail,
        @Email(message = "Business email must be valid") String businessMail,
        @NotNull(message = "Phone is required") String phone) {}
