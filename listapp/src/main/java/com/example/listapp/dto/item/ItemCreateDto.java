package com.example.listapp.dto.item;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ItemCreateDto(
    @NotBlank @Size(max = 100) String title,
    @Size(max = 2000) String notes,
    @Size(max = 1024) String imagePath
){}