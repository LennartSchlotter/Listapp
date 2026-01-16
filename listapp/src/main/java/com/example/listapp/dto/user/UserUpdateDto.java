package com.example.listapp.dto.user;

import java.util.Optional;

import com.example.listapp.dto.DtoConstraints;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UserUpdateDto(
    Optional<String> oauth2Provider,
    Optional<@Size(max = DtoConstraints.NAME_MAX_LENGTH) String> name,
    Optional<@Email @Size(max = DtoConstraints.EMAIL_MAX_LENGTH) String> email
) { }
