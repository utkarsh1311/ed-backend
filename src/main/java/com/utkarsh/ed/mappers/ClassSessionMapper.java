package com.utkarsh.ed.mappers;

import com.utkarsh.ed.dto.ClassSession.SessionDetailResponseDTO;
import com.utkarsh.ed.dto.ClassSession.SessionSummaryResponseDTO;
import com.utkarsh.ed.models.ClassSession;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClassSessionMapper {

    // 1. The Summary View (For Calendars and Lists)
    @Mapping(source = "classSchedule.id", target = "classScheduleId")
    @Mapping(source = "teacher.name", target = "teacherName")
    @Mapping(source = "classSchedule.student.name", target = "studentName")
    @Mapping(source = "classSchedule.subject.name", target = "subjectName")
    @Mapping(source = "test", target = "isTest")
    SessionSummaryResponseDTO toSummaryDTO(ClassSession session);

    // 2. The Detail View (For single session views and parent reports)
    @Mapping(source = "teacher.name", target = "teacherName")
    @Mapping(source = "classSchedule.student.name", target = "studentName")
    @Mapping(source = "classSchedule.subject.name", target = "subjectName")
    @Mapping(source = "test", target = "isTest")
    SessionDetailResponseDTO toDetailDTO(ClassSession session);
}
