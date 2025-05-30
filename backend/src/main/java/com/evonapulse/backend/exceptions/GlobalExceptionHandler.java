package com.evonapulse.backend.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiErrorBuilder> handleApiException(ApiException ex, HttpServletRequest request) {
        ApiErrorBuilder error = new ApiErrorBuilder(ex.getStatus(), ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(ex.getStatus()).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorBuilder> handleAll(Exception ex, HttpServletRequest request) {
        HttpStatus status = ex instanceof AccessDeniedException
                ? HttpStatus.FORBIDDEN
                : HttpStatus.INTERNAL_SERVER_ERROR;

        ApiErrorBuilder error = new ApiErrorBuilder(status, "An unexpected error occurred", request.getRequestURI());
        return ResponseEntity.status(status).body(error);
    }
}
