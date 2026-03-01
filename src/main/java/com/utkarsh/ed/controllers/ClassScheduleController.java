package com.utkarsh.ed.controllers;

import com.utkarsh.ed.dto.ClassSchedule.ClassScheduleFilter;
import com.utkarsh.ed.dto.ClassSchedule.ClassScheduleRequestDTO;
import com.utkarsh.ed.dto.ClassSchedule.ClassScheduleResponseDTO;
import com.utkarsh.ed.dto.PagedResponse;
import com.utkarsh.ed.services.ClassScheduleService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import org.springdoc.core.annotations.ParameterObject;

@Tag(name = "Class Schedules", description = "Recurring weekly class rules. Each schedule auto-generates ClassSessions.")
@RestController
@RequestMapping("/api/v1/class-schedules")
public class ClassScheduleController {

    private static final Logger logger = LoggerFactory.getLogger(ClassScheduleController.class);

    private final ClassScheduleService classScheduleService;

    public ClassScheduleController(ClassScheduleService classScheduleService) {
        this.classScheduleService = classScheduleService;
    }

    // Create class schedule
    @Operation(summary = "Create a class schedule",
        description = "Creates a recurring weekly schedule and immediately generates the first ClassSession.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Schedule created and first session generated"),
        @ApiResponse(responseCode = "400", description = "Validation failed",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Student, Teacher, or Subject not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "409", description = "Time slot conflicts with an existing schedule",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<ClassScheduleResponseDTO> createClassSchedule(
            @Valid @RequestBody ClassScheduleRequestDTO classScheduleRequestDTO) {

        ClassScheduleResponseDTO classSchedule = classScheduleService.createClassSchedule(classScheduleRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(classSchedule);
    }

    // Get all Class Schedules
    @Operation(summary = "List all class schedules", description = "Filterable, paginated list of all active recurring schedules.")
    @ApiResponse(responseCode = "200", description = "Page of schedules")
    @GetMapping
    public ResponseEntity<PagedResponse<ClassScheduleResponseDTO>> getAllClassSchedules(
            @ParameterObject @ModelAttribute ClassScheduleFilter filter, Pageable pageable) {
        Page<ClassScheduleResponseDTO> classSchedules = classScheduleService.getAllClassSchedules(filter, pageable);
        return ResponseEntity.ok(PagedResponse.from(classSchedules));
    }

    // Get class schedule by id
    @Operation(summary = "Get class schedule by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Schedule found"),
        @ApiResponse(responseCode = "404", description = "Schedule not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<ClassScheduleResponseDTO> getClassScheduleByID(@PathVariable Long id) {
        ClassScheduleResponseDTO classScheduleByID = classScheduleService.getClassScheduleByID(id);
        return ResponseEntity.ok(classScheduleByID);
    }

    @Operation(summary = "Update class schedule", description = "Partially updates a schedule. endTime is recalculated automatically from startTime + durationMinutes.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Schedule updated"),
        @ApiResponse(responseCode = "404", description = "Schedule not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "409", description = "Updated slot conflicts with an existing schedule",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping("/{id}")
    public ResponseEntity<ClassScheduleResponseDTO> updateClassSchedule(
            @PathVariable Long id, @Valid @RequestBody ClassScheduleRequestDTO classScheduleRequestDTO) {
        ClassScheduleResponseDTO classScheduleResponseDTO =
                classScheduleService.updateClassSchedule(id, classScheduleRequestDTO);
        return ResponseEntity.ok(classScheduleResponseDTO);
    }

    @Operation(summary = "Delete class schedule", description = "Soft-deletes the schedule and cancels all pending SCHEDULED sessions.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Schedule deleted"),
        @ApiResponse(responseCode = "404", description = "Schedule not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClassSchedule(@PathVariable Long id) {
        classScheduleService.deleteClassSchedule(id);
        return ResponseEntity.noContent().build();
    }
}
