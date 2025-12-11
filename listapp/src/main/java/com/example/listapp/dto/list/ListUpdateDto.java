package com.example.listapp.dto.list;

import java.util.Optional;
import jakarta.validation.constraints.Size;

public record ListUpdateDto(
    Optional<@Size(max = 100) String> title,
    Optional<@Size(max = 2000) String> description
){}
