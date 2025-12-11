package com.example.listapp.dto.user;

import java.util.Optional;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UserUpdateDto(
    Optional<String> oauth2_provider,
    Optional<@Size(max = 64) String> name,
    Optional<@Email @Size(max = 255) String> email
){}