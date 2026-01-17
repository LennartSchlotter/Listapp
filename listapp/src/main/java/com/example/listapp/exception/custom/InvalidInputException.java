package com.example.listapp.exception.custom;

import org.springframework.http.HttpStatus;

import com.example.listapp.exception.base.ApplicationException;

public class InvalidInputException extends ApplicationException {

    /**
     * Constructor for the Invalid Input Exception.
     * @param message string to be displayed alongside the exception.
     */
    public InvalidInputException(final String message) {
        super(message, HttpStatus.BAD_REQUEST, "INVALID_INPUT");
    }
}
