package com.utkarsh.ed.mappers;

import com.utkarsh.ed.dto.Student.StudentRequestDTO;
import com.utkarsh.ed.dto.Student.StudentResponseDTO;
import com.utkarsh.ed.models.Student;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StudentMapper {

	// CREATE
	@Mapping(target = "id", ignore = true)
	Student toEntity(StudentRequestDTO dto);

	// RESPONSE
	StudentResponseDTO toResponse(Student student);

	List<StudentResponseDTO> toResponseList(List<Student> students);

	// UPDATE (PATCH style)
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	@Mapping(target = "id", ignore = true)
	void updateFromDto(StudentRequestDTO dto, @MappingTarget Student student);
}