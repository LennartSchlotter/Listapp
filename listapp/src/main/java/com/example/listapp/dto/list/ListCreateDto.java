package com.example.listapp.dto.list;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ListCreateDto(
    @NotBlank @Size(max = 100) String title,
    @Size(max = 2000) String description
){}
