package com.example.listapp.exception.base;

import org.springframework.http.HttpStatus;

public class ApplicationException extends RuntimeException {

    /**
     * The Http Status for the error thrown.
     */
    private final HttpStatus status;

    /**
     * The error code for the error thrown.
     */
    private final String errorCode;

    /**
     * Constructor for the Application Exception.
     * @param messageField The message for the error.
     * @param statusField The Http Status for the error.
     * @param errorCodeField The error code for the error.
     */
    public ApplicationException(
        final String messageField,
        final HttpStatus statusField,
        final String errorCodeField
    ) {
        super(messageField);
        status = statusField;
        errorCode = errorCodeField;
    }

    /**
     * Constructor for the application exception including a throwable cause.
     * @param messageField The message for the error.
     * @param causeField The cause for the error.
     * @param statusField The Http Status for the error.
     * @param errorCodeField The error code for the error.
     */
    public ApplicationException(
        final String messageField,
        final Throwable causeField,
        final HttpStatus statusField,
        final String errorCodeField
    ) {
        super(messageField, causeField);
        status = statusField;
        errorCode = errorCodeField;
    }

    /**
     * Getter for the Status.
     * @return the status code.
     */
    public HttpStatus getStatus() {
        return this.status;
    }

    /***
     * Getter for the error code.
     * @return the error code.
     */
    public String getErrorCode() {
        return this.errorCode;
    }
}
