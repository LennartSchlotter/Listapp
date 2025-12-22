package com.example.listapp.util.dtos;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

import com.example.listapp.dto.list.ListSummaryDto;
import com.example.listapp.dto.user.UserResponseDto;

public class UserResponseDtoBuilder {
    private UUID id = UUID.randomUUID();
    private String name = "Test User";
    private String email = "testuser@example.com";
    private Instant createdAt = Instant.now();
    private Instant updatedAt = Instant.now();
    private Long version = 0L;
    private Set<ListSummaryDto> lists = Set.of();

    public static UserResponseDtoBuilder aUserResponseDto() {
        return new UserResponseDtoBuilder();
    }

    public UserResponseDtoBuilder withId(UUID id) {
        this.id = id;
        return this;
    }

    public UserResponseDtoBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public UserResponseDtoBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public UserResponseDtoBuilder withCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public UserResponseDtoBuilder withUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public UserResponseDto build() {
        return new UserResponseDto(id, name, email, createdAt, updatedAt, version, lists);
    }
}
