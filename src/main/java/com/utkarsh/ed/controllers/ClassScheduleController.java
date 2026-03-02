package com.utkarsh.ed.controllers;

import com.utkarsh.ed.dto.ClassSchedule.ClassScheduleFilter;
import com.utkarsh.ed.dto.ClassSchedule.ClassScheduleRequestDTO;
import com.utkarsh.ed.dto.ClassSchedule.ClassScheduleResponseDTO;
import com.utkarsh.ed.dto.PagedResponse;
import com.utkarsh.ed.models.AppUser;
import com.utkarsh.ed.services.ClassScheduleService;
import com.utkarsh.ed.swagger.ApiResponses404;
import com.utkarsh.ed.swagger.ApiResponsesCreate;
import com.utkarsh.ed.swagger.ApiResponsesDelete;
import com.utkarsh.ed.swagger.ApiResponsesUpdate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Class Schedules",
        description = "Recurring weekly class rules. Each schedule auto-generates ClassSessions.")
@RestController
@RequestMapping("/api/v1/class-schedules")
public class ClassScheduleController {

    private static final Logger logger = LoggerFactory.getLogger(ClassScheduleController.class);

    private final ClassScheduleService classScheduleService;

    public ClassScheduleController(ClassScheduleService classScheduleService) {
        this.classScheduleService = classScheduleService;
    }

    // Create class schedule
    @Operation(
            summary = "Create a class schedule",
            description = "Creates a recurring weekly schedule and immediately generates the first ClassSession.")
    @ApiResponsesCreate
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClassScheduleResponseDTO> createClassSchedule(
            @Valid @RequestBody ClassScheduleRequestDTO classScheduleRequestDTO) {

        ClassScheduleResponseDTO classSchedule = classScheduleService.createClassSchedule(classScheduleRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(classSchedule);
    }

    // Get all Class Schedules
    @Operation(summary = "List all class schedules")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN', 'TEACHER')")
    public ResponseEntity<PagedResponse<ClassScheduleResponseDTO>> getAllClassSchedules(
            @ParameterObject @ModelAttribute ClassScheduleFilter filter,
            Pageable pageable,
            @AuthenticationPrincipal AppUser currentUser) {

        Page<ClassScheduleResponseDTO> classSchedules =
                classScheduleService.getAllClassSchedules(filter, pageable, currentUser);
        return ResponseEntity.ok(PagedResponse.from(classSchedules));
    }

    // Get class schedule by id
    @Operation(summary = "Get class schedule by ID")
    @ApiResponses404
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN', 'TEACHER')")
    public ResponseEntity<ClassScheduleResponseDTO> getClassScheduleByID(
            @PathVariable Long id, @AuthenticationPrincipal AppUser currentUser) {

        ClassScheduleResponseDTO classScheduleByID = classScheduleService.getClassScheduleByID(id, currentUser);
        return ResponseEntity.ok(classScheduleByID);
    }

    @Operation(
            summary = "Update class schedule",
            description = "Partially updates a schedule. endTime is recalculated from startTime + durationMinutes.")
    @ApiResponsesUpdate
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClassScheduleResponseDTO> updateClassSchedule(
            @PathVariable Long id, @Valid @RequestBody ClassScheduleRequestDTO classScheduleRequestDTO) {

        ClassScheduleResponseDTO classScheduleResponseDTO =
                classScheduleService.updateClassSchedule(id, classScheduleRequestDTO);
        return ResponseEntity.ok(classScheduleResponseDTO);
    }

    @Operation(
            summary = "Delete class schedule",
            description = "Soft-deletes the schedule and cancels all pending sessions.")
    @ApiResponsesDelete
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteClassSchedule(@PathVariable Long id) {

        classScheduleService.deleteClassSchedule(id);
        return ResponseEntity.noContent().build();
    }
}
