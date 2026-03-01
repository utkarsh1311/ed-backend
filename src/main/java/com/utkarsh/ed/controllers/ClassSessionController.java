package com.utkarsh.ed.controllers;

import com.utkarsh.ed.dto.ClassSession.ClassSessionFilter;
import com.utkarsh.ed.dto.ClassSession.SessionCancellationRequestDTO;
import com.utkarsh.ed.dto.ClassSession.SessionCompletionRequestDTO;
import com.utkarsh.ed.dto.ClassSession.SessionDetailResponseDTO;
import com.utkarsh.ed.dto.ClassSession.SessionRescheduleRequestDTO;
import com.utkarsh.ed.dto.PagedResponse;
import com.utkarsh.ed.services.ClassSessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Class Sessions", description = "Individual session occurrences. Auto-generated from Class Schedules weekly.")
@RestController
@RequestMapping("/api/v1/class-sessions")
public class ClassSessionController {

    private final ClassSessionService classSessionService;

    public ClassSessionController(ClassSessionService classSessionService) {
        this.classSessionService = classSessionService;
    }

    @Operation(summary = "List all class sessions",
        description = "Filterable and paginated. Date range filters on scheduledAt (current actual slot).")
    @GetMapping
    public ResponseEntity<PagedResponse<SessionDetailResponseDTO>> getAllSessions(
            @ParameterObject @ModelAttribute ClassSessionFilter filter, Pageable pageable) {
        Page<SessionDetailResponseDTO> sessions = classSessionService.getAllSessions(filter, pageable);
        return ResponseEntity.ok(PagedResponse.from(sessions));
    }

    // TODO: teacherID should come from JWT principal once auth is implemented
    @Operation(summary = "Complete a session",
        description = "Marks a SCHEDULED or RESCHEDULED session as COMPLETED. Only the assigned teacher may complete a session.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Session completed"),
        @ApiResponse(responseCode = "400", description = "Validation failed (e.g. end before start, missing test score)"),
        @ApiResponse(responseCode = "403", description = "Caller is not the assigned teacher"),
        @ApiResponse(responseCode = "404", description = "Session not found"),
        @ApiResponse(responseCode = "409", description = "Session is already cancelled")
    })
    @PostMapping("/{id}/complete")
    public ResponseEntity<SessionDetailResponseDTO> completeSession(
            @PathVariable Long id,
            @RequestParam Long teacherID,
            @RequestBody SessionCompletionRequestDTO sessionCompletionRequestDTO) {
        SessionDetailResponseDTO sessionDetailResponseDTO =
                classSessionService.completeSession(id, teacherID, sessionCompletionRequestDTO);
        return ResponseEntity.ok(sessionDetailResponseDTO);
    }

    @Operation(summary = "Cancel a session")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Session cancelled"),
        @ApiResponse(responseCode = "404", description = "Session not found"),
        @ApiResponse(responseCode = "409", description = "Session is already cancelled or completed")
    })
    @PostMapping("/{id}/cancel")
    public ResponseEntity<SessionDetailResponseDTO> cancelSession(
            @PathVariable Long id, @Valid @RequestBody SessionCancellationRequestDTO cancellationRequestDTO) {
        SessionDetailResponseDTO cancelledSession = classSessionService.cancelSession(id, cancellationRequestDTO);
        return ResponseEntity.ok(cancelledSession);
    }

    @Operation(summary = "Reschedule a session",
        description = "New time must be in the future and within 7 days of current scheduledAt. Validates no teacher/student conflicts.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Session rescheduled"),
        @ApiResponse(responseCode = "400", description = "New time is not in the future"),
        @ApiResponse(responseCode = "404", description = "Session not found"),
        @ApiResponse(responseCode = "409", description = "Session is cancelled or completed"),
        @ApiResponse(responseCode = "422", description = "Same slot / outside week boundary / teacher or student conflict")
    })
    @PostMapping("/{id}/reschedule")
    public ResponseEntity<SessionDetailResponseDTO> rescheduleSession(
            @PathVariable Long id, @Valid @RequestBody SessionRescheduleRequestDTO rescheduleRequestDTO) {
        SessionDetailResponseDTO rescheduledSession = classSessionService.rescheduleSession(id, rescheduleRequestDTO);
        return ResponseEntity.ok(rescheduledSession);
    }
}
