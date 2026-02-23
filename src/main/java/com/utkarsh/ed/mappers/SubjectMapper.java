package com.utkarsh.ed.mappers;

import com.utkarsh.ed.dto.Subject.SubjectRequestDTO;
import com.utkarsh.ed.dto.Subject.SubjectResponseDTO;
import com.utkarsh.ed.models.Subject;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubjectMapper {

    // CREATE
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    Subject toEntity(SubjectRequestDTO dto);

    // RESPONSE
    SubjectResponseDTO toResponse(Subject subject);

    List<SubjectResponseDTO> toResponseList(List<Subject> subjects);

    // UPDATE (PATCH style)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    void updateFromDto(SubjectRequestDTO dto, @MappingTarget Subject subject);
}
