package com.husyairi.ManaProgressAku.ExceptionHandling;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
            default -> status = HttpStatus.BAD_REQUEST; // fallback
        }

        return new ResponseEntity<>(errorResponse, status);
    }
}

