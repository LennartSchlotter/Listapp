package com.example.listapp.dto.item;

import java.util.Optional;

import com.example.listapp.helper.Constraints;

import jakarta.validation.constraints.Size;

public record ItemUpdateDto(
    Optional<@Size(max = Constraints.TITLE_MAX_LENGTH) String> title,
    Optional<@Size(max = Constraints.NOTES_MAX_LENGTH) String> notes,
    Optional<@Size(max =
        Constraints.IMAGE_PATH_MAX_LENGTH) String> imagePath
) { }
