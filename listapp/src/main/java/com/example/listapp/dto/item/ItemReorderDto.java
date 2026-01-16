package com.example.listapp.dto.item;

import java.util.List;
import java.util.UUID;

public record ItemReorderDto(List<UUID> itemOrder) { }
