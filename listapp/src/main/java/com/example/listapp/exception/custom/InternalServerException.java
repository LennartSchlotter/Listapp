package com.example.listapp.exception.custom;

import org.springframework.http.HttpStatus;

import com.example.listapp.exception.base.ApplicationException;

public class InternalServerException extends ApplicationException {

    /**
     * Constructor for the Internal Server Exception.
     * @param message string to be displayed alongside the exception.
     * @param cause cause for the exception.
     */
    public InternalServerException(
        final String message,
        final Throwable cause
    ) {
        super(
            message,
            cause,
            HttpStatus.INTERNAL_SERVER_ERROR,
            "INTERNAL_SERVER_ERROR"
        );
    }
}
