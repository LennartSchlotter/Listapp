package com.example.listapp.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.example.listapp.dto.error.ErrorResponseDto;
import com.example.listapp.exception.base.ApplicationException;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Handles custom application exceptions.
     * @param ex The Exception thrown.
     * @param request The request that caused the exception.
     * @return an entity containing the response and status of the exception.
     */
    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponseDto> handleApplicationException(
        final ApplicationException ex, final WebRequest request) {

        log.error("Application exception occured: {}", ex.getMessage(), ex);

        ErrorResponseDto errorResponse = new ErrorResponseDto(
            ex.getStatus().value(),
            ex.getMessage(),
            ex.getStatus().getReasonPhrase(),
            ex.getErrorCode()
        );

        return new ResponseEntity<>(errorResponse, ex.getStatus());
    }

    /**
     * Handles validation errors.
     * @param ex The Exception thrown.
     * @param request The request that caused the exception.
     * @return an entity containing the response and status of the exception.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationException(
        final MethodArgumentNotValidException ex, final WebRequest request) {

        log.error("Validation error occured", ex);

        Map<String, String> validationErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
            validationErrors.put(error.getField(), error.getDefaultMessage())
        );

        ErrorResponseDto errorResponse = new ErrorResponseDto(
            HttpStatus.BAD_REQUEST.value(),
            HttpStatus.BAD_REQUEST.getReasonPhrase(),
            "Validation failed",
            "VALIDATION_ERROR"
        );

        Map<String, String> errors = new HashMap<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        errorResponse.setValidationErrors(errors);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles constraint violations.
     * @param ex The Exception thrown.
     * @param request The request that caused the exception.
     * @return an entity containing the response and status of the exception.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponseDto> handleConstraintViolation(
        final ConstraintViolationException ex, final WebRequest request) {

        log.error("Constraint violation occured", ex);

        Map<String, String> validationErrors = new HashMap<>();
        ex.getConstraintViolations().forEach(violation ->
            validationErrors.put(
                violation.getPropertyPath().toString(),
                violation.getMessage()
            )
        );

        ErrorResponseDto errorResponse = new ErrorResponseDto(
            HttpStatus.BAD_REQUEST.value(),
            HttpStatus.BAD_REQUEST.getReasonPhrase(),
            "Constraint violation",
            "CONSTRAINT_VIOLATION"
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles missing request parameters.
     * @param ex The Exception thrown.
     * @param request The request that caused the exception.
     * @return an entity containing the response and status of the exception.
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponseDto> handleMissingParams(
        final MissingServletRequestParameterException ex,
        final WebRequest request
    ) {

        log.error("Missing request parameters: {}", ex.getParameterName(), ex);

        ErrorResponseDto errorResponse = new ErrorResponseDto(
            HttpStatus.BAD_REQUEST.value(),
            HttpStatus.BAD_REQUEST.getReasonPhrase(),
            String.format(
                "Required parameter '%s' is missing",
                ex.getParameterName()
            ),
            "MISSING_PARAMETER"
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles access denied exceptions.
     * @param ex The Exception thrown.
     * @param request The request that caused the exception.
     * @return an entity containing the response and status of the exception.
     */
    @ExceptionHandler(
        org.springframework.security.access.AccessDeniedException.class)
    public ResponseEntity<ErrorResponseDto>
        handleSpringSecurityAccessDenied(
        final org.springframework.security.access.AccessDeniedException ex,
        final WebRequest request
    ) {

    log.warn("Access denied: {}", ex.getMessage());

    ErrorResponseDto errorResponse = new ErrorResponseDto(
        HttpStatus.FORBIDDEN.value(),
        HttpStatus.FORBIDDEN.getReasonPhrase(),
        "You don't have permission to access this resource",
        "ACCESS_DENIED"
    );

    return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    /**
     * Handles argument type mismatch exceptions.
     * @param ex The Exception thrown.
     * @param request The request that caused the exception.
     * @return an entity containing the response and status of the exception.
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponseDto> handleTypeMismatch(
        final MethodArgumentTypeMismatchException ex,
        final WebRequest request
    ) {

        log.error("Method argument type mismatch: {}", ex.getMessage(), ex);

        ErrorResponseDto errorResponse = new ErrorResponseDto(
            HttpStatus.BAD_REQUEST.value(),
            HttpStatus.BAD_REQUEST.getReasonPhrase(),
            "Validation failed",
            "VALIDATION_ERROR"
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles all other exceptions.
     * @param ex The Exception thrown.
     * @param request The request that caused the exception.
     * @return an entity containing the response and status of the exception.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGlobalException(
        final Exception ex, final WebRequest request) {

        log.error("Unexpected error occured", ex);

        ErrorResponseDto errorResponse = new ErrorResponseDto(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
            "An unexpected error occured",
            "INTERNAL_SERVER_ERROR"
        );

        return new ResponseEntity<>(
            errorResponse,
            HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
