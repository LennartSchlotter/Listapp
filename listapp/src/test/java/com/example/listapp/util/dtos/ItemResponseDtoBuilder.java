package com.example.listapp.util.dtos;

import java.time.Instant;
import java.util.UUID;

import com.example.listapp.dto.item.ItemResponseDto;

public class ItemResponseDtoBuilder {
    private UUID id = UUID.randomUUID();
    private String title = "Test Items";
    private String notes = "Test Notes";
    private String imagePath = null;
    private Integer position = 0;
    private Instant createdAt = Instant.now();
    private Instant updatedAt = Instant.now();
    private Long version = 0L;

    public static ItemResponseDtoBuilder anItemResponseDto() {
        return new ItemResponseDtoBuilder();
    }

    public ItemResponseDtoBuilder withId(UUID id) {
        this.id = id;
        return this;
    }

    public ItemResponseDtoBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public ItemResponseDtoBuilder withNotes(String notes) {
        this.notes = notes;
        return this;
    }
    
    public ItemResponseDtoBuilder withPosition(Integer position) {
        this.position = position;
        return this;
    }

    public ItemResponseDtoBuilder withCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public ItemResponseDtoBuilder withUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public ItemResponseDto build() {
        return new ItemResponseDto(id, title, notes, imagePath, position, createdAt, updatedAt, version);
    }
}
