package com.example.listapp.util.builder;

import java.util.UUID;

import com.example.listapp.entity.User;

public class UserTestBuilder {
    private UUID id = UUID.randomUUID();
    private String oauth2Provider = "google";
    private String oauth2Sub = "oauth-sub-123";
    private String name = "testuser";
    private String email = "testuser@example.com";

    public static UserTestBuilder aUser() {
        return new UserTestBuilder();
    }

    public UserTestBuilder withId(UUID id) {
        this.id = id;
        return this;
    }

    public UserTestBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public UserTestBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public User build() {
        User user = new User();
        user.setId(id);
        user.setOauth2Provider(oauth2Provider);
        user.setOauth2Sub(oauth2Sub);
        user.setName(name);
        user.setEmail(email);
        return user;
    }
}
