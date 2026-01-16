package com.example.listapp.dto.list;

import java.util.Optional;

import com.example.listapp.helper.Constraints;

import jakarta.validation.constraints.Size;

public record ListUpdateDto(
    Optional<@Size(max = Constraints.TITLE_MAX_LENGTH) String> title,
    Optional<@Size(max =
        Constraints.DESCRIPTION_MAX_LENGTH) String> description
) { }
