package com.example.listapp.dto.user;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

import com.example.listapp.dto.list.ListSummaryDto;

public record UserResponseDto(
    UUID id,
    String name,
    String email,
    Instant createdAt,
    Instant updatedAt,
    Long version,
    Set<ListSummaryDto> lists
) {

    /**
     * Safely copies the list as immutability guarantee.
     */
    public UserResponseDto {
        lists = lists == null ? Set.of() : Set.copyOf(lists);
    }
}
