package com.utkarsh.ed.services;

import com.utkarsh.ed.dto.ClassSchedule.ClassScheduleFilter;
import com.utkarsh.ed.dto.ClassSchedule.ClassScheduleRequestDTO;
import com.utkarsh.ed.dto.ClassSchedule.ClassScheduleResponseDTO;
import com.utkarsh.ed.exceptions.BusinessRuleException;
import com.utkarsh.ed.mappers.ClassScheduleMapper;
import com.utkarsh.ed.models.ClassSchedule;
import com.utkarsh.ed.repositories.ClassScheduleRepository;
import com.utkarsh.ed.repositories.ClassScheduleSpecification;
import java.time.LocalTime;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ClassScheduleService {

	private static final Logger logger = LoggerFactory.getLogger(ClassScheduleService.class);

	private final ClassScheduleRepository classScheduleRepository;
	private final ClassScheduleMapper classScheduleMapper;

	public ClassScheduleService(
			ClassScheduleRepository classScheduleRepository,
			ClassScheduleMapper classScheduleMapper) {
		this.classScheduleRepository = classScheduleRepository;
		this.classScheduleMapper = classScheduleMapper;
	}

	// Create Class Schedule
	public ClassScheduleResponseDTO createClassSchedule(
			ClassScheduleRequestDTO scheduleRequestDTO) {

		LocalTime calculateEndTime = scheduleRequestDTO.startTime().plusMinutes(scheduleRequestDTO.durationMinutes());

		boolean hasConflict = classScheduleRepository.existsOverlappingSchedule(
				scheduleRequestDTO.teacherId(),
				scheduleRequestDTO.studentId(),
				scheduleRequestDTO.dayOfWeek(),
				scheduleRequestDTO.startTime(),
				calculateEndTime);

		if (hasConflict) {
			logger.warn(
					"Schedule conflict detected for Teacher {} or Student {} on {}",
					scheduleRequestDTO.teacherId(),
					scheduleRequestDTO.studentId(),
					scheduleRequestDTO.dayOfWeek());
			throw new BusinessRuleException(
					"The teacher or student already has a class scheduled during this time.");
		}

		ClassSchedule newClassSchedule = classScheduleRepository.save(classScheduleMapper.toEntity(scheduleRequestDTO));
		logger.info(
				"ClassSchedule created for teacher {} and student {}",
				newClassSchedule.getTeacher().getName(),
				newClassSchedule.getStudent().getName());
		return classScheduleMapper.toResponse(newClassSchedule);
	}

	// Get all classSchedules with optional filters
	public List<ClassScheduleResponseDTO> getAllClassSchedules(ClassScheduleFilter filter) {
		Specification<ClassSchedule> spec = ClassScheduleSpecification.build(filter);
		return classScheduleRepository.findAll(spec)
				.stream()
				.map(classScheduleMapper::toResponse)
				.toList();
	}

}
