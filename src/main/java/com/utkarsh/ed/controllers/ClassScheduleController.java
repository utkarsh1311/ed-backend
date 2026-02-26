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
            @Valid @RequestBody ClassScheduleRequestDTO classScheduleRequestDTO) {

        ClassScheduleResponseDTO classSchedule = classScheduleService.createClassSchedule(classScheduleRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(classSchedule);
    }

    // Get all Class Schedules
    @GetMapping
    public ResponseEntity<PagedResponse<ClassScheduleResponseDTO>> getAllClassSchedules(
            @ModelAttribute ClassScheduleFilter filter, Pageable pageable) {
        Page<ClassScheduleResponseDTO> classSchedules = classScheduleService.getAllClassSchedules(filter, pageable);
        return ResponseEntity.ok(PagedResponse.from(classSchedules));
    }

    // Get class schedule by id
    @GetMapping("/{id}")
    public ResponseEntity<ClassScheduleResponseDTO> getClassScheduleByID(@PathVariable Long id) {
        ClassScheduleResponseDTO classScheduleByID = classScheduleService.getClassScheduleByID(id);
        return ResponseEntity.ok(classScheduleByID);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ClassScheduleResponseDTO> updateClassSchedule(
            @PathVariable Long id, @RequestBody ClassScheduleRequestDTO classScheduleRequestDTO) {
        ClassScheduleResponseDTO classScheduleResponseDTO =
                classScheduleService.updateClassSchedule(id, classScheduleRequestDTO);
        return ResponseEntity.ok(classScheduleResponseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClassSchedule(@PathVariable Long id) {
        classScheduleService.deleteClassSchedule(id);
        return ResponseEntity.noContent().build();
    }
}
