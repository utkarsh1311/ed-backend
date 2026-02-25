package com.utkarsh.ed.controllers;

import com.utkarsh.ed.dto.ClassSession.ClassSessionFilter;
import com.utkarsh.ed.dto.ClassSession.SessionDetailResponseDTO;
import com.utkarsh.ed.dto.PagedResponse;
import com.utkarsh.ed.services.ClassSessionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/class-sessions")
public class ClassSessionController {

    private final ClassSessionService classSessionService;

    public ClassSessionController(ClassSessionService classSessionService) {
        this.classSessionService = classSessionService;
    }

    @GetMapping
    public ResponseEntity<PagedResponse<SessionDetailResponseDTO>> getAllSessions(
            @ModelAttribute ClassSessionFilter filter, Pageable pageable) {
        Page<SessionDetailResponseDTO> sessions = classSessionService.getAllSessions(filter, pageable);
        return ResponseEntity.ok(PagedResponse.from(sessions));
    }
}
