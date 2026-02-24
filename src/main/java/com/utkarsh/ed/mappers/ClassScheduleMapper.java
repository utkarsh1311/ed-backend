package com.utkarsh.ed.mappers;

import com.utkarsh.ed.dto.ClassSchedule.ClassScheduleRequestDTO;
import com.utkarsh.ed.dto.ClassSchedule.ClassScheduleResponseDTO;
import com.utkarsh.ed.models.ClassSchedule;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ClassScheduleMapper {

	ClassScheduleResponseDTO toResponse(ClassSchedule classSchedule);

	ClassSchedule toEntity(ClassScheduleRequestDTO classScheduleRequestDTO);

	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	@Mapping(target = "id", ignore = true)
	void updateFromDto(ClassScheduleRequestDTO dto, @MappingTarget ClassSchedule classSchedule);
}
