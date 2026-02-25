package com.utkarsh.ed.mappers;

import com.utkarsh.ed.dto.ClassSchedule.ClassScheduleRequestDTO;
import com.utkarsh.ed.dto.ClassSchedule.ClassScheduleResponseDTO;
import com.utkarsh.ed.models.ClassSchedule;
import com.utkarsh.ed.models.Student;
import com.utkarsh.ed.models.Subject;
import com.utkarsh.ed.models.Teacher;
import com.utkarsh.ed.repositories.StudentRepository;
import com.utkarsh.ed.repositories.SubjectRepository;
import com.utkarsh.ed.repositories.TeacherRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public abstract class ClassScheduleMapper {

    @Autowired
    protected StudentRepository studentRepository;

    @Autowired
    protected TeacherRepository teacherRepository;

    @Autowired
    protected SubjectRepository subjectRepository;

    public abstract ClassScheduleResponseDTO toResponse(ClassSchedule classSchedule);

    @Mapping(target = "student", source = "studentId")
    @Mapping(target = "teacher", source = "teacherId")
    @Mapping(target = "subject", source = "subjectId")
    @Mapping(target = "endTime", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    public abstract ClassSchedule toEntity(ClassScheduleRequestDTO classScheduleRequestDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "student", source = "studentId")
    @Mapping(target = "teacher", source = "teacherId")
    @Mapping(target = "subject", source = "subjectId")
    @Mapping(target = "endTime", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    public abstract void updateFromDto(ClassScheduleRequestDTO dto, @MappingTarget ClassSchedule classSchedule);

    protected Student mapStudent(Long studentId) {
        if (studentId == null) return null;
        return studentRepository.getReferenceById(studentId);
    }

    protected Teacher mapTeacher(Long teacherId) {
        if (teacherId == null) return null;
        return teacherRepository.getReferenceById(teacherId);
    }

    protected Subject mapSubject(Long subjectId) {
        if (subjectId == null) return null;
        return subjectRepository.getReferenceById(subjectId);
    }
}
