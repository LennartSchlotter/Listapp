package com.example.listapp.exception.custom;

import org.springframework.http.HttpStatus;

import com.example.listapp.exception.base.ApplicationException;

public class ResourceAlreadyExistsException extends ApplicationException {

    /**
     * Constructor for the Resource already exists Exception.
     * @param resourceName The name of the resource that already exists.
     * @param identifier The identifier of the resource that already exists.
     */
    public ResourceAlreadyExistsException(
        final String resourceName,
        final String identifier
    ) {
        super(
            String.format(
                "%s already exists with identifier: %s",
                resourceName,
                identifier
            ),
            HttpStatus.CONFLICT,
            "RESOURCE_ALREADY_EXISTS"
        );
    }
}
