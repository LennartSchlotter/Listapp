package com.example.listapp.exception.base;

import org.springframework.http.HttpStatus;

public class ApplicationException extends RuntimeException {
    private final HttpStatus _status;
    private final String _errorCode;

    public ApplicationException(String message, HttpStatus status, String errorCode) {
        super(message);
        _status = status;
        _errorCode = errorCode;
    }

    public ApplicationException(String message, Throwable cause, HttpStatus status, String errorCode) {
        super(message, cause);
        _status = status;
        _errorCode = errorCode;
    }

    public HttpStatus getStatus() {
        return _status;
    }

    public String getErrorCode() {
        return _errorCode;
    }
}
