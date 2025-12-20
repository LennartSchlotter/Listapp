package com.example.listapp.exception.custom;

import org.springframework.http.HttpStatus;

import com.example.listapp.exception.base.ApplicationException;

public class ResourceAlreadyExistsException extends ApplicationException {
    public ResourceAlreadyExistsException(String resourceName, String identifier) {
        super(String.format("%s already exists with identifier: %s", resourceName, identifier),HttpStatus.CONFLICT, "RESOURCE_ALREADY_EXISTS");
    }
}
