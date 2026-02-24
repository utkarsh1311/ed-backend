package com.utkarsh.ed.dto.ClassSchedule;

import com.utkarsh.ed.models.ScheduleStatus;
import com.utkarsh.ed.models.WeekDay;

public record ClassScheduleFilter(
        Long teacherId, Long studentId, Long subjectId, WeekDay dayOfWeek, ScheduleStatus status) {
}
