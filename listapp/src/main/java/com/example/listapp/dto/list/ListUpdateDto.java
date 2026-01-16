package com.example.listapp.dto.list;

import java.util.Optional;

import com.example.listapp.dto.DtoConstraints;

import jakarta.validation.constraints.Size;

public record ListUpdateDto(
    Optional<@Size(max = DtoConstraints.TITLE_MAX_LENGTH) String> title,
    Optional<@Size(max =
        DtoConstraints.DESCRIPTION_MAX_LENGTH) String> description
) { }
