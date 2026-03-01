package com.utkarsh.ed.dto.Student;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Request body for creating or updating a student")
public record StudentRequestDTO(
        @Schema(description = "Student's full name", example = "Aryan Sharma")
        @NotBlank(message = "Name is required") String name,
        @Schema(description = "Student's email address", example = "aryan.sharma@gmail.com")
        @NotBlank(message = "Email is required") @Email(message = "Email must be valid") String email,
        @Schema(description = "Parent's email for notifications", example = "parent.sharma@gmail.com")
        @Email(message = "Parent email must be valid") String parentEmail,
        @Schema(description = "Parent's phone number (Australian format preferred)", example = "+61412345678")
        String parentPhone,
        @Schema(description = "Father's name", example = "Rajesh Sharma")
        String fatherName,
        @Schema(description = "Mother's name", example = "Priya Sharma")
        String motherName,
        @Schema(description = "Grade level (1–12)", example = "10")
        @NotNull(message = "Grade is required")
                @Min(value = 1, message = "Grade must be at least 1")
                @Max(value = 12, message = "Grade must be at most 12")
                Integer grade) {}
