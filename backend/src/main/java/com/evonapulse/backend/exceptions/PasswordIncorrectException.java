package com.evonapulse.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class PasswordIncorrectException extends RuntimeException {
    public PasswordIncorrectException() {
        super("Invalid password");
    }
}
