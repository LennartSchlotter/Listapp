package com.example.listapp.dto.error;

import java.time.LocalDateTime;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponseDto {
    private int status;
    private String error;
    private String message;
    private String errorCode;
    private LocalDateTime timestamp;
    private Map<String, String> validationErrors;

    public ErrorResponseDto(int status, String error, String message, String errorCode) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.errorCode = errorCode;
        timestamp = LocalDateTime.now();
    }
}
