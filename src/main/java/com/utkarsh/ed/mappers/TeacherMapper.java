package com.utkarsh.ed.mappers;

import com.utkarsh.ed.dto.Teacher.TeacherRequestDTO;
import com.utkarsh.ed.dto.Teacher.TeacherResponseDTO;
import com.utkarsh.ed.dto.Teacher.TeacherSummaryDTO;
import com.utkarsh.ed.models.Teacher;
import java.util.List;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface TeacherMapper {

    // CREATE
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "availabilities", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    Teacher toEntity(TeacherRequestDTO dto);

    // RESPONSE
    TeacherResponseDTO toResponse(Teacher teacher);

    List<TeacherResponseDTO> toResponseList(List<Teacher> teachers);

    // SUMMARY
    TeacherSummaryDTO toSummary(Teacher teacher);

    // UPDATE (PATCH style)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "availabilities", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    void updateFromDto(TeacherRequestDTO dto, @MappingTarget Teacher teacher);
}
