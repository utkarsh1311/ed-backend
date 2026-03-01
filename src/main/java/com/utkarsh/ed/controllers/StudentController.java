package com.utkarsh.ed.controllers;

import com.utkarsh.ed.dto.PagedResponse;
import com.utkarsh.ed.dto.Student.StudentRequestDTO;
import com.utkarsh.ed.dto.Student.StudentResponseDTO;
import com.utkarsh.ed.services.StudentService;
import com.utkarsh.ed.swagger.ApiResponsesCreate;
import com.utkarsh.ed.swagger.ApiResponsesDelete;
import com.utkarsh.ed.swagger.ApiResponses404;
import com.utkarsh.ed.swagger.ApiResponsesUpdate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Students", description = "Manage student profiles (Australia-based)")
@RestController
@RequestMapping("/api/v1/students")
public class StudentController {

    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @Operation(summary = "Create a student", description = "Registers a new student. Email must be unique.")
    @ApiResponsesCreate
    @PostMapping
    public ResponseEntity<StudentResponseDTO> createStudent(@Valid @RequestBody StudentRequestDTO requestDTO) {
        logger.debug("Creating student with email: {}", requestDTO.email());
        StudentResponseDTO createdStudent = studentService.createStudent(requestDTO);
        logger.info("Student created successfully with ID: {}", createdStudent.id());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdStudent);
    }

    @Operation(summary = "List all students")
    @GetMapping
    public ResponseEntity<PagedResponse<StudentResponseDTO>> getAllStudents(
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        logger.info("Fetching students - page: {}, size: {}", pageable.getPageNumber(), pageable.getPageSize());
        Page<StudentResponseDTO> students = studentService.getAllStudents(pageable);
        logger.info(
                "Found {} students (page {} of {})",
                students.getNumberOfElements(),
                pageable.getPageNumber(),
                students.getTotalPages());
        return ResponseEntity.ok(PagedResponse.from(students));
    }

    @Operation(summary = "Get student by ID")
    @ApiResponses404
    @GetMapping("/{id}")
    public ResponseEntity<StudentResponseDTO> getStudentByID(@PathVariable Long id) {
        logger.info("Fetching student with ID: {}", id);
        StudentResponseDTO student = studentService.getStudentById(id);
        logger.info("Student found: {}", student.name());
        return ResponseEntity.ok(student);
    }

    @Operation(summary = "Update student")
    @ApiResponsesUpdate
    @PatchMapping("/{id}")
    public ResponseEntity<StudentResponseDTO> updateStudent(
            @PathVariable Long id, @Valid @RequestBody StudentRequestDTO requestDTO) {
        logger.info("Updating student with ID: {}", id);
        StudentResponseDTO updatedStudent = studentService.updateStudent(id, requestDTO);
        logger.info("Student with id: {} updated successfully", id);
        return ResponseEntity.ok(updatedStudent);
    }

    @Operation(summary = "Delete student", description = "Soft-deletes the student (sets deletedAt).")
    @ApiResponsesDelete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }
}
