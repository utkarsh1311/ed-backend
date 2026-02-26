package com.utkarsh.ed.dto.ClassSchedule;

import com.utkarsh.ed.dto.Student.StudentSummaryDTO;
import com.utkarsh.ed.dto.Subject.SubjectSummaryDTO;
import com.utkarsh.ed.dto.Teacher.TeacherSummaryDTO;
import java.time.DayOfWeek;
import java.time.LocalTime;

public record ClassScheduleResponseDTO(
        Long id,
        StudentSummaryDTO student,
        TeacherSummaryDTO teacher,
        SubjectSummaryDTO subject,
        DayOfWeek dayOfWeek,
        LocalTime startTime,
        LocalTime endTime,
        int durationMinutes,
        String meetLink) {}
