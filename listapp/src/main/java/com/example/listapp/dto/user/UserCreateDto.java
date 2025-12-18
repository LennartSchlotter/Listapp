package com.example.listapp.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserCreateDto(
    String oauth2Provider,
    @NotBlank String oauth2Sub,
    @NotBlank @Size(max = 64) String name,
    @NotBlank @Email @Size(max = 255) String email
){}