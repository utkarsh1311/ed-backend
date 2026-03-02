package com.utkarsh.ed.dto.ClassSchedule;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.time.LocalTime;
import org.hibernate.validator.constraints.URL;

@Schema(description = "Request body for creating or updating a recurring class schedule")
public record ClassScheduleRequestDTO(
        @Schema(description = "ID of the student", example = "1") @NotNull(message = "Student ID is required")
                Long studentId,
        @Schema(description = "ID of the teacher", example = "2") @NotNull(message = "Teacher ID is required")
                Long teacherId,
        @Schema(description = "ID of the subject", example = "3") @NotNull(message = "Subject ID is required")
                Long subjectId,
        @Schema(description = "Day of the week the class recurs", example = "MONDAY")
                @NotNull(message = "Day of the week is required")
                DayOfWeek dayOfWeek,
        @Schema(description = "Class start time in IST (HH:mm)", example = "14:00:00")
                @NotNull(message = "Start time is required")
                LocalTime startTime,
        @Schema(description = "Duration in minutes. Defaults to 60 if omitted. Minimum 60.", example = "60")
                @Min(value = 60, message = "Duration must be at least 60 minutes")
                Integer durationMinutes,
        @Schema(
                        description = "Google Meet or Zoom link for the session",
                        example = "https://meet.google.com/abc-defg-hij")
                @NotBlank(message = "Meeting link cannot be empty")
                @URL(message = "Meeting link must be a valid URL")
                String meetLink) {
    // This is a "Compact Constructor".
    // It runs automatically when Spring creates the Record from the incoming JSON.
    public ClassScheduleRequestDTO {
        // Handle default values if the client forgets to send durationMinutes
        if (durationMinutes == null) {
            durationMinutes = 60;
        }
    }
}
