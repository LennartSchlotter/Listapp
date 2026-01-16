package com.example.listapp.dto.user;

import com.example.listapp.helper.Constraints;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserCreateDto(
    String oauth2Provider,
    @NotBlank String oauth2Sub,
    @NotBlank @Size(max = Constraints.NAME_MAX_LENGTH) String name,
    @NotBlank @Email @Size(max = Constraints.EMAIL_MAX_LENGTH) String email
) { }
