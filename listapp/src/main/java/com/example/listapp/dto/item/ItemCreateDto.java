package com.example.listapp.dto.item;

import com.example.listapp.helper.Constraints;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ItemCreateDto(
    @NotBlank @Size(max = Constraints.TITLE_MAX_LENGTH) String title,
    @Size(max = Constraints.NOTES_MAX_LENGTH) String notes,
    @Size(max = Constraints.IMAGE_PATH_MAX_LENGTH) String imagePath
) { }
