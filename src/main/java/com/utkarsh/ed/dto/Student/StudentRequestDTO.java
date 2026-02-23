package com.utkarsh.ed.dto.Student;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record StudentRequestDTO(
        @NotBlank(message = "Name is required")
        String name,

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        String email,

        @Email(message = "Parent email must be valid")
        String parentEmail,

        String parentPhone,

        String fatherName,

        String motherName,

        @NotNull(message = "Grade is required")
        @Min(value = 1, message = "Grade must be at least 1")
        @Max(value = 12, message = "Grade must be at most 12")
        Integer grade
) {
}
