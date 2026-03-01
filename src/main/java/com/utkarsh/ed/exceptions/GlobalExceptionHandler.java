package com.utkarsh.ed.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.LinkedHashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // ── Domain exceptions ──────────────────────────────────────────────────────

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex, HttpServletRequest req) {
        return build(HttpStatus.NOT_FOUND, "Not Found", ex.getMessage(), req);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateResource(
            DuplicateResourceException ex, HttpServletRequest req) {
        return build(HttpStatus.CONFLICT, "Conflict", ex.getMessage(), req);
    }

    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<ErrorResponse> handleBusinessRule(BusinessRuleException ex, HttpServletRequest req) {
        return build(HttpStatus.UNPROCESSABLE_ENTITY, "Unprocessable Entity", ex.getMessage(), req);
    }

    // ── Validation exceptions ──────────────────────────────────────────────────

    /**
     * Handles @Valid failures on @RequestBody — returns a map of field → rejected message.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpServletRequest req) {
        Map<String, String> fieldErrors = new LinkedHashMap<>();
        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.putIfAbsent(fe.getField(), fe.getDefaultMessage());
        }
        ErrorResponse err = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Validation Failed",
                "%d field(s) failed validation".formatted(fieldErrors.size()),
                req.getRequestURI(),
                fieldErrors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

    /**
     * Handles @Validated failures on path/query parameters.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(
            ConstraintViolationException ex, HttpServletRequest req) {
        Map<String, String> fieldErrors = new LinkedHashMap<>();
        for (ConstraintViolation<?> cv : ex.getConstraintViolations()) {
            String field = cv.getPropertyPath().toString();
            fieldErrors.putIfAbsent(field, cv.getMessage());
        }
        ErrorResponse err = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Validation Failed",
                "%d constraint(s) violated".formatted(fieldErrors.size()),
                req.getRequestURI(),
                fieldErrors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

    // ── HTTP / request exceptions ──────────────────────────────────────────────

    /** Malformed or unreadable JSON body. */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleNotReadable(HttpMessageNotReadableException ex, HttpServletRequest req) {
        return build(HttpStatus.BAD_REQUEST, "Bad Request", "Malformed or unreadable request body.", req);
    }

    /** Wrong HTTP method (e.g. GET on a POST-only endpoint). */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotSupported(
            HttpRequestMethodNotSupportedException ex, HttpServletRequest req) {
        String msg = "HTTP method '%s' is not supported for this endpoint.".formatted(ex.getMethod());
        return build(HttpStatus.METHOD_NOT_ALLOWED, "Method Not Allowed", msg, req);
    }

    /** Wrong Content-Type header. */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException ex, HttpServletRequest req) {
        String msg = "Content-Type '%s' is not supported.".formatted(ex.getContentType());
        return build(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Unsupported Media Type", msg, req);
    }

    /** Missing required @RequestParam. */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingParam(
            MissingServletRequestParameterException ex, HttpServletRequest req) {
        String msg = "Required parameter '%s' of type '%s' is missing."
                .formatted(ex.getParameterName(), ex.getParameterType());
        return build(HttpStatus.BAD_REQUEST, "Bad Request", msg, req);
    }

    /** Path variable or request param type mismatch (e.g. string where Long expected). */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex, HttpServletRequest req) {
        String expected = ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown";
        String msg = "Parameter '%s' must be of type '%s'.".formatted(ex.getName(), expected);
        return build(HttpStatus.BAD_REQUEST, "Bad Request", msg, req);
    }

    /** Static resource / route not found (replaces deprecated NoHandlerFoundException in Boot 3.x). */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResourceFound(NoResourceFoundException ex, HttpServletRequest req) {
        return build(HttpStatus.NOT_FOUND, "Not Found", "No endpoint found for this request.", req);
    }

    // ── Data / state exceptions ────────────────────────────────────────────────

    /** DB unique-constraint or FK violation. */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrity(
            DataIntegrityViolationException ex, HttpServletRequest req) {
        logger.warn(
                "Data integrity violation on {}: {}",
                req.getRequestURI(),
                ex.getMostSpecificCause().getMessage());
        return build(
                HttpStatus.CONFLICT,
                "Conflict",
                "The request conflicts with existing data. Please verify your input.",
                req);
    }

    /** Application-level illegal state (e.g. completing an already-completed session). */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalState(IllegalStateException ex, HttpServletRequest req) {
        return build(HttpStatus.CONFLICT, "Conflict", ex.getMessage(), req);
    }

    /** Application-level illegal argument (bad input that slipped past bean validation). */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest req) {
        return build(HttpStatus.BAD_REQUEST, "Bad Request", ex.getMessage(), req);
    }

    /** Caller tried to perform an action they are not permitted to. */
    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<ErrorResponse> handleSecurity(SecurityException ex, HttpServletRequest req) {
        logger.warn("Security violation on {}: {}", req.getRequestURI(), ex.getMessage());
        return build(HttpStatus.FORBIDDEN, "Forbidden", "You do not have permission to perform this action.", req);
    }

    // ── Catch-all ──────────────────────────────────────────────────────────────

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex, HttpServletRequest req) {
        logger.error("Unhandled exception on [{}] {}: {}", req.getMethod(), req.getRequestURI(), ex.getMessage(), ex);
        return build(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal Server Error",
                "An unexpected error occurred. Please try again later.",
                req);
    }

    // ── Helper ─────────────────────────────────────────────────────────────────

    private ResponseEntity<ErrorResponse> build(
            HttpStatus status, String error, String message, HttpServletRequest req) {
        ErrorResponse body = new ErrorResponse(status.value(), error, message, req.getRequestURI());
        return ResponseEntity.status(status).body(body);
    }
}
