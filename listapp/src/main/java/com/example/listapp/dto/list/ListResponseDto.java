package com.example.listapp.dto.list;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

import com.example.listapp.dto.item.ItemSummaryDto;
import com.example.listapp.dto.user.UserSummaryDto;

public record ListResponseDto(
    UUID id,
    String title,
    String description,
    Instant createdAt,
    Instant updatedAt,
    Long version,
    UserSummaryDto owner,
    Set<ItemSummaryDto> items
) {

    /**
     * Safely copies the list as immutability guarantee.
     */
    public ListResponseDto {
        items = items == null ? Set.of() : Set.copyOf(items);
    }
}
