package com.example.listapp.dto.item;

import com.example.listapp.dto.DtoConstraints;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ItemCreateDto(
    @NotBlank @Size(max = DtoConstraints.TITLE_MAX_LENGTH) String title,
    @Size(max = DtoConstraints.NOTES_MAX_LENGTH) String notes,
    @Size(max = DtoConstraints.IMAGE_PATH_MAX_LENGTH) String imagePath
) { }
