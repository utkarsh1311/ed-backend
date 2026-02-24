package com.utkarsh.ed.dto.ClassSchedule;

import com.utkarsh.ed.models.WeekDay;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;
import org.hibernate.validator.constraints.URL;

public record ClassScheduleRequestDTO(
		@NotNull(message = "Student ID is required") Long studentId,
		@NotNull(message = "Teacher ID is required") Long teacherId,
		@NotNull(message = "Subject ID is required") Long subjectId,
		@NotNull(message = "Day of the week is required") WeekDay dayOfWeek,
		@NotNull(message = "Start time is required") LocalTime startTime,
		@Min(value = 60, message = "Duration must be at least 60 minutes") Integer durationMinutes,
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
