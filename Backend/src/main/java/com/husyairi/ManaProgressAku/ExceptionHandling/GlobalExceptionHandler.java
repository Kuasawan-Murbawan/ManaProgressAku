package com.husyairi.ManaProgressAku.ExceptionHandling;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.security.access.AccessDeniedException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiErrorResponse> handleBadRequestException(BadRequestException exception) {
        BadRequestException raisedException = (BadRequestException) exception;

        ApiErrorResponse errorResponse = new ApiErrorResponse(
                raisedException.getErrorId(),
                raisedException.getErrorMessage(),
                raisedException.getErrorDetails()
        );
        

        // Dynamically map error ID to correct HTTP status
        HttpStatus status;

        switch (raisedException.getErrorId()) {
            case 400 -> status = HttpStatus.BAD_REQUEST;
            case 404 -> status = HttpStatus.NOT_FOUND;
            case 403 -> status = HttpStatus.FORBIDDEN;
            case 401 -> status = HttpStatus.UNAUTHORIZED;
            case 409 -> status = HttpStatus.CONFLICT;
            case 500 -> status = HttpStatus.INTERNAL_SERVER_ERROR;
            case 429 -> status = HttpStatus.TOO_MANY_REQUESTS;
            case 502 -> status = HttpStatus.BAD_GATEWAY;
            default -> status = HttpStatus.BAD_REQUEST; // fallback
        }

        return new ResponseEntity<>(errorResponse, status);
    }
    // Handles @Valid failures on @RequestBody DTOs — e.g. @DecimalMin, @DecimalMax, @Past
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationException(MethodArgumentNotValidException exception) {
        Map<String, Object> fieldErrors = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(error ->
                fieldErrors.put(error.getField(), error.getDefaultMessage())
        );

        ApiErrorResponse errorResponse = new ApiErrorResponse(
                400,
                "Validation failed",
                fieldErrors
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // Handles malformed JSON or an invalid enum value (e.g. gender: "MAN")
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleUnreadableBody(HttpMessageNotReadableException exception) {
        ApiErrorResponse errorResponse = new ApiErrorResponse(
                400,
                "Malformed request body",
                new HashMap<>()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // @PreAuthorize denials (e.g. a regular user calling an admin endpoint).
    // Handled here so Spring Security's default sendError(403) never triggers the
    // /error replay that rewrites the status to 401.
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiErrorResponse> handleAccessDenied(AccessDeniedException exception) {
        ApiErrorResponse errorResponse = new ApiErrorResponse(
                403,
                "You do not have permission to perform this action",
                new HashMap<>()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }
}

