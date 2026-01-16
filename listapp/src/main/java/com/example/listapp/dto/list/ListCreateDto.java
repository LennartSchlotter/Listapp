package com.example.listapp.dto.list;

import com.example.listapp.dto.DtoConstraints;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ListCreateDto(
    @NotBlank @Size(max = DtoConstraints.TITLE_MAX_LENGTH) String title,
    @Size(max = DtoConstraints.DESCRIPTION_MAX_LENGTH) String description
) { }
