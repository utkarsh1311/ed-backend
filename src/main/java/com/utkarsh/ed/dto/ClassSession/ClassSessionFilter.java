package com.utkarsh.ed.dto.ClassSession;

import com.utkarsh.ed.models.SessionStatus;
import com.utkarsh.ed.models.WeekDay;
import java.time.LocalDateTime;
import java.util.List;

public record ClassSessionFilter(
        List<Long> teacherIds,
        List<Long> studentIds,
        List<Long> subjectIds,
        List<WeekDay> weekdays,
        List<SessionStatus> sessionStatuses,
        LocalDateTime scheduledFrom,
        LocalDateTime scheduledTo) {}
