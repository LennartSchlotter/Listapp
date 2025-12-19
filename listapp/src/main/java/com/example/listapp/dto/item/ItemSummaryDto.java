package com.example.listapp.dto.item;

import java.util.UUID;

public record ItemSummaryDto(UUID id, String title, Integer position, String imagePath) {}