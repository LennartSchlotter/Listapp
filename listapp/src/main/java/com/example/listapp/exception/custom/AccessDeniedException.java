package com.example.listapp.exception.custom;

import org.springframework.http.HttpStatus;

import com.example.listapp.exception.base.ApplicationException;

public class AccessDeniedException extends ApplicationException {

    /**
     * Constructor for the Access Denied Exception.
     * @param message message to be thrown alongisde the exception.
     */
    public AccessDeniedException(final String message) {
        super(message, HttpStatus.FORBIDDEN, "ACCESS_DENIED");
    }
}
