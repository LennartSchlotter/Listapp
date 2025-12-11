package com.example.listapp.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserCreateDto(
    String oauth2_provider,
    @NotBlank String oauth2_sub,
    @NotBlank @Size(max = 64) String name,
    @NotBlank @Email @Size(max = 255) String email
){}