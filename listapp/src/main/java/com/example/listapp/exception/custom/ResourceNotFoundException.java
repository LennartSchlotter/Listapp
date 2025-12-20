package com.example.listapp.exception.custom;

import org.springframework.http.HttpStatus;

import com.example.listapp.exception.base.ApplicationException;

public class ResourceNotFoundException extends ApplicationException {
    public ResourceNotFoundException(String resourceName, String identifier) {
        super(String.format("%s not found with identifier: %s", resourceName, identifier), HttpStatus.NOT_FOUND, "RESOURCE_NOT_FOUND");
    }
}
