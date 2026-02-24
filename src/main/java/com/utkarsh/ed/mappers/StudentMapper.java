package com.utkarsh.ed.mappers;

import com.utkarsh.ed.dto.Student.StudentRequestDTO;
import com.utkarsh.ed.dto.Student.StudentResponseDTO;
import com.utkarsh.ed.dto.Student.StudentSummaryDTO;
import com.utkarsh.ed.models.Student;
import java.util.List;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    // CREATE
    @Mapping(target = "id", ignore = true)
    Student toEntity(StudentRequestDTO dto);

    // RESPONSE
    StudentResponseDTO toResponse(Student student);

    List<StudentResponseDTO> toResponseList(List<Student> students);

    // SUMMARY
    StudentSummaryDTO toSummary(Student student);

    // UPDATE (PATCH style)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updateFromDto(StudentRequestDTO dto, @MappingTarget Student student);
}
