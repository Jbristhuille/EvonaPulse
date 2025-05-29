package com.evonapulse.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ProjectNameAlreadyExistsException extends RuntimeException {
    public ProjectNameAlreadyExistsException(String name) {
        super("A project with the name '" + name + "' already exists.");
    }
}
