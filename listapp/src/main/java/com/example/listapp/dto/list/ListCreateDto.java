package com.example.listapp.dto.list;

import com.example.listapp.helper.Constraints;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ListCreateDto(
    @NotBlank @Size(max = Constraints.TITLE_MAX_LENGTH) String title,
    @Size(max = Constraints.DESCRIPTION_MAX_LENGTH) String description
) { }
