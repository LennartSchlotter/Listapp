package com.example.listapp.util.dtos;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.example.listapp.dto.item.ItemSummaryDto;
import com.example.listapp.dto.list.ListResponseDto;
import com.example.listapp.dto.user.UserSummaryDto;

public class ListResponseDtoBuilder {
    private UUID id = UUID.randomUUID();
    private String title = "Test List";
    private String description = "Test Description";
    private Instant createdAt = Instant.now();
    private Instant updatedAt = Instant.now();
    private Long version = 0L;
    private UserSummaryDto owner = new UserSummaryDto(UUID.randomUUID(), "Test User");
    private Set<ItemSummaryDto> items = Set.of();

    public static ListResponseDtoBuilder aListResponseDto() {
        return new ListResponseDtoBuilder();
    }

    public ListResponseDtoBuilder withId(UUID id) {
        this.id = id;
        return this;
    }

    public ListResponseDtoBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public ListResponseDtoBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public ListResponseDtoBuilder withCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public ListResponseDtoBuilder withUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public ListResponseDtoBuilder withOwner(UserSummaryDto owner) {
        this.owner = owner;
        return this;
    }

    public ListResponseDtoBuilder withItems(Set<ItemSummaryDto> items) {
        this.items = items;
        return this;
    }

    public ListResponseDtoBuilder withItem(ItemSummaryDto item) {
        if (this.items.isEmpty()) {
            this.items = new HashSet<>();
        }
        this.items.add(item);
        return this;
    }

    public ListResponseDto build() {
        return new ListResponseDto(id, title, description, createdAt, updatedAt, version, owner, items);
    }
}
