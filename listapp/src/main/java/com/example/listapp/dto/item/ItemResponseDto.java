package com.example.listapp.dto.item;

import java.time.Instant;
import java.util.UUID;

public record ItemResponseDto(
    UUID id,
    String title,
    String notes,
    String imagePath,
    Integer position,
    Instant createdAt,
    Instant updatedAt,
    Long version
){}