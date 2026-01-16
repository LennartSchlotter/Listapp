package com.example.listapp.dto.item;

import java.util.Optional;

import com.example.listapp.dto.DtoConstraints;

import jakarta.validation.constraints.Size;

public record ItemUpdateDto(
    Optional<@Size(max = DtoConstraints.TITLE_MAX_LENGTH) String> title,
    Optional<@Size(max = DtoConstraints.NOTES_MAX_LENGTH) String> notes,
    Optional<@Size(max =
        DtoConstraints.IMAGE_PATH_MAX_LENGTH) String> imagePath
) { }
