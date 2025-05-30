package com.evonapulse.backend.exceptions;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@Data
public class ApiErrorBuilder {
    private final String timestamp;
    private final int status;
    private final String error;
    private final String message;
    private final String path;

    public ApiErrorBuilder(HttpStatus status, String message, String path) {
        this.timestamp = Instant.now().toString();
        this.status = status.value();
        this.error = status.getReasonPhrase();
        this.message = message;
        this.path = path;
    }
}
