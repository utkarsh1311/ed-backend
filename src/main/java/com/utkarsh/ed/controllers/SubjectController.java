package com.utkarsh.ed.controllers;

import com.utkarsh.ed.dto.Subject.SubjectRequestDTO;
import com.utkarsh.ed.dto.Subject.SubjectResponseDTO;
import com.utkarsh.ed.services.SubjectService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/subjects")
public class SubjectController {

    private static final Logger logger = LoggerFactory.getLogger(SubjectController.class);
    private final SubjectService subjectService;

    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }


    @PostMapping
    public ResponseEntity<SubjectResponseDTO> createSubject(@Valid @RequestBody SubjectRequestDTO requestDTO) {
        logger.debug("Creating subject with name: {}", requestDTO.name());
        SubjectResponseDTO created = subjectService.createSubject(requestDTO);
        logger.info("Subject created successfully with ID: {}", created.id());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<SubjectResponseDTO>> getAllSubjects() {
        return ResponseEntity.ok(subjectService.getAllSubjects());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubjectResponseDTO> getSubjectById(@PathVariable Long id) {
        logger.info("Fetching subject with ID: {}", id);
        SubjectResponseDTO subject = subjectService.getSubjectById(id);
        logger.info("Subject found: {}", subject.name());
        return ResponseEntity.ok(subject);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<SubjectResponseDTO> updateSubject(
            @PathVariable Long id,
            @Valid @RequestBody SubjectRequestDTO requestDTO) {
        logger.info("Updating subject with ID: {}", id);
        SubjectResponseDTO updated = subjectService.updateSubject(id, requestDTO);
        logger.info("Subject with ID: {} updated successfully", id);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubject(@PathVariable Long id) {
        logger.info("Deleting subject with ID: {}", id);
        subjectService.deleteSubject(id);
        return ResponseEntity.noContent().build();
    }
}
