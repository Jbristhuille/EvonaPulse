package com.evonapulse.backend.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorBuilder> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .findFirst()
                .orElse("Validation failed");

        ApiErrorBuilder error = new ApiErrorBuilder(HttpStatus.BAD_REQUEST, message, request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiErrorBuilder> handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        String param = ex.getName();
        String value = String.valueOf(ex.getValue());
        String msg = "Invalid value for parameter '" + param + "': '" + value + "'";

        ApiErrorBuilder error = new ApiErrorBuilder(HttpStatus.BAD_REQUEST, msg, request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseEntity<ApiErrorBuilder> handleMissingPathVariable(MissingPathVariableException ex, HttpServletRequest request) {
        String msg = "Missing path variable: " + ex.getVariableName();

        ApiErrorBuilder error = new ApiErrorBuilder(HttpStatus.BAD_REQUEST, msg, request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

}
