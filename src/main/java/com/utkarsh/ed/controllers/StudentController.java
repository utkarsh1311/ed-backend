package com.utkarsh.ed.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.utkarsh.ed.dto.PagedResponse;
import com.utkarsh.ed.dto.Student.StudentRequestDTO;
import com.utkarsh.ed.dto.Student.StudentResponseDTO;
import com.utkarsh.ed.services.StudentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/students")
public class StudentController {

	private static final Logger logger = LoggerFactory.getLogger(StudentController.class);
	private final StudentService studentService;

	public StudentController(StudentService studentService) {
		this.studentService = studentService;
	}

	@PostMapping
	public ResponseEntity<StudentResponseDTO> createStudent(@Valid @RequestBody StudentRequestDTO requestDTO) {
		logger.debug("Creating student with email: {}", requestDTO.email());
		StudentResponseDTO createdStudent = studentService.createStudent(requestDTO);
		logger.info("Student created successfully with ID: {}", createdStudent.id());
		return ResponseEntity.status(HttpStatus.CREATED).body(createdStudent);
	}

	@GetMapping
	public ResponseEntity<PagedResponse<StudentResponseDTO>> getAllStudents(
			@PageableDefault(size = 20, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
		logger.info("Fetching students - page: {}, size: {}", pageable.getPageNumber(), pageable.getPageSize());
		Page<StudentResponseDTO> students = studentService.getAllStudents(pageable);
		logger.info("Found {} students (page {} of {})", students.getNumberOfElements(), pageable.getPageNumber(), students.getTotalPages());
		return ResponseEntity.ok(PagedResponse.from(students));
	}

	@GetMapping("/{id}")
    public ResponseEntity<StudentResponseDTO> getStudentByID(@PathVariable Long id) {
        logger.info("Fetching student with ID: {}", id);
        StudentResponseDTO student = studentService.getStudentById(id);
        logger.info("Student found: {}", student.name());
        return ResponseEntity.ok(student);
    }

	@PatchMapping("/{id}")
	public ResponseEntity<StudentResponseDTO> updateStudent(@PathVariable Long id, @Valid @RequestBody StudentRequestDTO requestDTO) {
		logger.info("Updating student with ID: {}", id);
		StudentResponseDTO updatedStudent = studentService.updateStudent(id, requestDTO);
		logger.info("Student with id: {} updated successfully", id);
		return ResponseEntity.ok(updatedStudent);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
		studentService.deleteStudent(id);
		return ResponseEntity.noContent().build();
	}

}
