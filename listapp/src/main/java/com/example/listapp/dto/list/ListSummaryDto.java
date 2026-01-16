package com.example.listapp.dto.list;

import java.util.UUID;

public record ListSummaryDto(UUID id, String title, int itemCount) { }
