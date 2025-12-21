package com.example.listapp.dto.item;

import java.util.Optional;
import jakarta.validation.constraints.Size;

public record ItemUpdateDto(
    Optional<@Size(max = 100) String> title,
    Optional<@Size(max = 2000) String> notes,
    Optional<@Size(max = 1024) String> imagePath
){}
