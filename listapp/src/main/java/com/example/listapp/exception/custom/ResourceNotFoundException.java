package com.example.listapp.exception.custom;

import org.springframework.http.HttpStatus;

import com.example.listapp.exception.base.ApplicationException;

public class ResourceNotFoundException extends ApplicationException {

    /**
     * Constructor for the Resource not found Exception.
     * @param resourceName name of the resource not found.
     * @param identifier identifier of the resource not found.
     */
    public ResourceNotFoundException(
        final String resourceName,
        final String identifier
    ) {
        super(
            String.format(
                "%s not found with identifier: %s",
                resourceName,
                identifier
            ),
            HttpStatus.NOT_FOUND,
            "RESOURCE_NOT_FOUND"
        );
    }
}
