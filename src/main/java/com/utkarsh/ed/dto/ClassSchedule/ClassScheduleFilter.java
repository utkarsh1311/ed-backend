package com.utkarsh.ed.dto.ClassSchedule;

import com.utkarsh.ed.models.ScheduleStatus;
import java.time.DayOfWeek;

public record ClassScheduleFilter(
        Long teacherId, Long studentId, Long subjectId, DayOfWeek dayOfWeek, ScheduleStatus status) {}
