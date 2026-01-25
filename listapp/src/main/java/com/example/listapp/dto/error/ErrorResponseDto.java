package com.example.listapp.dto.error;

import java.time.LocalDateTime;
import java.util.Map;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ErrorResponseDto {

    /**
     * The status value of the error thrown.
     */
    private int status;

    /**
     * The error string.
     */
    private String error;

    /**
     * The message to be displayed.
     */
    private String message;

    /**
     * The error code.
     */
    private String errorCode;

    /**
     * The timestamp of when the error occured.
     */
    private LocalDateTime timestamp;

    /**
     * A map of validation errors.
     */
    private Map<String, String> validationErrors = Map.of();

    /**
     * Constructor for the ErrorResponseDTO.
     * @param statusField Field for the status thrown.
     * @param errorField Field for the error value.
     * @param messageField Field for the message to be displayed.
     * @param errorCodeField Field for the error code.
     */
    public ErrorResponseDto(
            final int statusField,
            final String errorField,
            final String messageField,
            final String errorCodeField
        ) {
        status = statusField;
        error = errorField;
        message = messageField;
        errorCode = errorCodeField;
        timestamp = LocalDateTime.now();
        validationErrors = Map.of();
    }

    /**
     * Setter for validation errors.
     * @param validationErrorsField a map of validation errors to set.
     */
    public void setValidationErrors(
        final Map<String, String> validationErrorsField) {
        this.validationErrors = validationErrorsField == null
            ? Map.of()
            : Map.copyOf(validationErrorsField);
    }
}
