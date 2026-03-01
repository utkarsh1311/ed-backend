package com.utkarsh.ed.controllers;

import com.utkarsh.ed.dto.ClassSession.ClassSessionFilter;
import com.utkarsh.ed.dto.ClassSession.SessionCancellationRequestDTO;
import com.utkarsh.ed.dto.ClassSession.SessionCompletionRequestDTO;
import com.utkarsh.ed.dto.ClassSession.SessionDetailResponseDTO;
import com.utkarsh.ed.dto.ClassSession.SessionRescheduleRequestDTO;
import com.utkarsh.ed.dto.PagedResponse;
import com.utkarsh.ed.services.ClassSessionService;
import jakarta.validation.Valid;
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

    // needs to be fixed as teacherID should be passed from context
    // or maybe I should pass the userdetails and let service figure it out
    @PostMapping("/{id}/complete")
    public ResponseEntity<SessionDetailResponseDTO> completeSession(
            @PathVariable Long id,
            @RequestParam Long teacherID,
            @RequestBody SessionCompletionRequestDTO sessionCompletionRequestDTO) {
        SessionDetailResponseDTO sessionDetailResponseDTO =
                classSessionService.completeSession(id, teacherID, sessionCompletionRequestDTO);
        return ResponseEntity.ok(sessionDetailResponseDTO);
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<SessionDetailResponseDTO> cancelSession(
            @PathVariable Long id, @Valid @RequestBody SessionCancellationRequestDTO cancellationRequestDTO) {
        SessionDetailResponseDTO cancelledSession = classSessionService.cancelSession(id, cancellationRequestDTO);
        return ResponseEntity.ok(cancelledSession);
    }

    @PostMapping("/{id}/reschedule")
    public ResponseEntity<SessionDetailResponseDTO> rescheduleSession(
            @PathVariable Long id, @Valid @RequestBody SessionRescheduleRequestDTO rescheduleRequestDTO) {
        SessionDetailResponseDTO rescheduledSession = classSessionService.rescheduleSession(id, rescheduleRequestDTO);
        return ResponseEntity.ok(rescheduledSession);
    }
}
