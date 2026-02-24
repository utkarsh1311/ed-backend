package com.utkarsh.ed.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.utkarsh.ed.dto.ClassSchedule.ClassScheduleFilter;
import com.utkarsh.ed.dto.ClassSchedule.ClassScheduleRequestDTO;
import com.utkarsh.ed.dto.ClassSchedule.ClassScheduleResponseDTO;
import com.utkarsh.ed.services.ClassScheduleService;

@RestController
@RequestMapping("/api/v1/class-schedules")
public class ClassScheduleController {
	private static final Logger logger = LoggerFactory.getLogger(ClassScheduleController.class);

	private final ClassScheduleService classScheduleService;

	public ClassScheduleController(ClassScheduleService classScheduleService) {
		this.classScheduleService = classScheduleService;
	}

	// Create class schedule
	@PostMapping
	public ResponseEntity<ClassScheduleResponseDTO> createClassSchedule(
			@RequestBody ClassScheduleRequestDTO classScheduleRequestDTO) {

		ClassScheduleResponseDTO classSchedule = classScheduleService.createClassSchedule(classScheduleRequestDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(classSchedule);
	}

	@GetMapping
	public ResponseEntity<List<ClassScheduleResponseDTO>> getAllClassSchedules(
			@ModelAttribute ClassScheduleFilter filter) {
		List<ClassScheduleResponseDTO> classSchedules = classScheduleService.getAllClassSchedules(filter);
		return ResponseEntity.ok(classSchedules);
	}

}
