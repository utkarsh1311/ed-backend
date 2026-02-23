package com.utkarsh.ed.mappers;

import com.utkarsh.ed.dto.Teacher.TeacherRequestDTO;
import com.utkarsh.ed.dto.Teacher.TeacherResponseDTO;
import com.utkarsh.ed.models.Teacher;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TeacherMapper {

	// CREATE
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "availabilities", ignore = true)
	Teacher toEntity(TeacherRequestDTO dto);

	// RESPONSE
	TeacherResponseDTO toResponse(Teacher teacher);

	List<TeacherResponseDTO> toResponseList(List<Teacher> teachers);

	// UPDATE (PATCH style)
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "availabilities", ignore = true)
	void updateFromDto(TeacherRequestDTO dto, @MappingTarget Teacher teacher);
}
