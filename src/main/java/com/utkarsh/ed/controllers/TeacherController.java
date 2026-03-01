package com.utkarsh.ed.controllers;

import com.utkarsh.ed.dto.PagedResponse;
import com.utkarsh.ed.dto.Teacher.TeacherRequestDTO;
import com.utkarsh.ed.dto.Teacher.TeacherResponseDTO;
import com.utkarsh.ed.services.TeacherService;
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

import com.utkarsh.ed.exceptions.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Teachers", description = "Manage teacher profiles (India-based)")
@RestController
@RequestMapping("/api/v1/teachers")
public class TeacherController {
    private static final Logger logger = LoggerFactory.getLogger(TeacherController.class);
    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @Operation(summary = "Create a teacher", description = "Registers a new teacher. Both emails must be unique.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Teacher created successfully"),
        @ApiResponse(responseCode = "400", description = "Validation failed",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "409", description = "Email already registered",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<TeacherResponseDTO> createTeacher(@Valid @RequestBody TeacherRequestDTO teacherRequestDTO) {
        logger.debug("Creating Teacher with email: {}", teacherRequestDTO.businessMail());
        TeacherResponseDTO teacherResponseDTO = teacherService.createTeacher(teacherRequestDTO);
        logger.info("Teacher created successfully with ID: {}", teacherResponseDTO.id());
        return ResponseEntity.status(HttpStatus.CREATED).body(teacherResponseDTO);
    }

    @Operation(summary = "List all teachers", description = "Paginated list of all active teachers.")
    @ApiResponse(responseCode = "200", description = "Page of teachers")
    @GetMapping
    public ResponseEntity<PagedResponse<TeacherResponseDTO>> getAllTeachers(
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        logger.info("Fetching teachers - page: {}, size: {}", pageable.getPageNumber(), pageable.getPageSize());
        Page<TeacherResponseDTO> teachers = teacherService.getAllTeachers(pageable);
        logger.info(
                "Found {} teachers (page {} of {})",
                teachers.getNumberOfElements(),
                pageable.getPageNumber(),
                teachers.getTotalPages());
        return ResponseEntity.ok(PagedResponse.from(teachers));
    }

    @Operation(summary = "Get teacher by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Teacher found"),
        @ApiResponse(responseCode = "404", description = "Teacher not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<TeacherResponseDTO> getTeacherById(@PathVariable Long id) {
        logger.info("Fetching teacher with ID: {}", id);
        TeacherResponseDTO teacher = teacherService.getTeacherById(id);
        logger.info("Teacher found: {}", teacher.name());
        return ResponseEntity.ok(teacher);
    }

    @Operation(summary = "Update teacher", description = "Partially updates a teacher's details.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Teacher updated"),
        @ApiResponse(responseCode = "400", description = "Validation failed",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Teacher not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping("/{id}")
    public ResponseEntity<TeacherResponseDTO> updateTeacher(
            @PathVariable Long id, @Valid @RequestBody TeacherRequestDTO teacherRequestDTO) {
        logger.info("Updating teacher with ID: {}", id);
        TeacherResponseDTO updatedTeacher = teacherService.updateTeacher(id, teacherRequestDTO);
        logger.info("Teacher with id: {} updated successfully", id);
        return ResponseEntity.ok(updatedTeacher);
    }

    @Operation(summary = "Delete teacher", description = "Soft-deletes the teacher and their availabilities.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Teacher deleted"),
        @ApiResponse(responseCode = "404", description = "Teacher not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable Long id) {
        teacherService.deleteTeacher(id);
        return ResponseEntity.noContent().build();
    }
}
