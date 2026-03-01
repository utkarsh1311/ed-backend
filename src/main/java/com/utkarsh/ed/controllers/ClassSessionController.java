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

import com.utkarsh.ed.exceptions.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;

@Tag(name = "Class Sessions", description = "Individual session occurrences. Auto-generated from Class Schedules weekly.")
@RestController
@RequestMapping("/api/v1/class-sessions")
public class ClassSessionController {

    private final ClassSessionService classSessionService;

    public ClassSessionController(ClassSessionService classSessionService) {
        this.classSessionService = classSessionService;
    }

    @Operation(summary = "List all class sessions",
        description = "Filterable, paginated list of sessions. Filter by teacher, student, subject, weekday, status, or date range. Date range filters on `scheduledAt` (current actual slot).")
    @ApiResponse(responseCode = "200", description = "Page of sessions")
    @GetMapping
    public ResponseEntity<PagedResponse<SessionDetailResponseDTO>> getAllSessions(
            @ParameterObject @ModelAttribute ClassSessionFilter filter, Pageable pageable) {
        Page<SessionDetailResponseDTO> sessions = classSessionService.getAllSessions(filter, pageable);
        return ResponseEntity.ok(PagedResponse.from(sessions));
    }

    // TODO: teacherID should come from JWT principal once auth is implemented
    @Operation(summary = "Complete a session",
        description = "Marks a SCHEDULED or RESCHEDULED session as COMPLETED. Records actual start/end times and optional test score. Only the assigned teacher may complete a session.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Session completed"),
        @ApiResponse(responseCode = "400", description = "Validation failed (e.g. end before start, missing test score)",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "403", description = "Caller is not the assigned teacher",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Session not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "409", description = "Session is already cancelled",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
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

    @Operation(summary = "Cancel a session", description = "Cancels a SCHEDULED or RESCHEDULED session. Completed sessions cannot be cancelled.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Session cancelled"),
        @ApiResponse(responseCode = "404", description = "Session not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "409", description = "Session is already cancelled or completed",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/{id}/cancel")
    public ResponseEntity<SessionDetailResponseDTO> cancelSession(
            @PathVariable Long id, @Valid @RequestBody SessionCancellationRequestDTO cancellationRequestDTO) {
        SessionDetailResponseDTO cancelledSession = classSessionService.cancelSession(id, cancellationRequestDTO);
        return ResponseEntity.ok(cancelledSession);
    }

    @Operation(summary = "Reschedule a session",
        description = """
            Moves a SCHEDULED or RESCHEDULED session to a new time slot. Business rules:
            - New time must be in the future (`@Future` validated)
            - New time must be within 7 days of the current `scheduledAt` (same occurrence window)
            - No teacher or student overlap with another active session in the new slot
            The previous `scheduledAt` is saved in `originalScheduledAt` for audit purposes.
            """)
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Session rescheduled"),
        @ApiResponse(responseCode = "400", description = "New time is not in the future",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Session not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "409", description = "Session is cancelled or completed",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "422", description = "Business rule violation: same slot / outside week / teacher or student conflict",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/{id}/reschedule")
    public ResponseEntity<SessionDetailResponseDTO> rescheduleSession(
            @PathVariable Long id, @Valid @RequestBody SessionRescheduleRequestDTO rescheduleRequestDTO) {
        SessionDetailResponseDTO rescheduledSession = classSessionService.rescheduleSession(id, rescheduleRequestDTO);
        return ResponseEntity.ok(rescheduledSession);
    }
}
