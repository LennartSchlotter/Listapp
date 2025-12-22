package com.example.listapp.util.entities;

import static com.example.listapp.util.entities.UserTestBuilder.aUser;

import java.util.UUID;

import com.example.listapp.entity.ListEntity;
import com.example.listapp.entity.User;

public class ListTestBuilder {
    private UUID id;
    private User owner = aUser().build();
    private String title = "Test list";
    private String description = "Test description";
    private boolean deleted = false;

    public static ListTestBuilder aList() {
        return new ListTestBuilder();
    }

    public ListTestBuilder withId(UUID id) {
        this.id = id;
        return this;
    }

    public ListTestBuilder withOwner(User owner) {
        this.owner = owner;
        return this;
    }

    public ListTestBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public ListTestBuilder deleted() {
        this.deleted = true;
        return this;
    }

    public ListEntity build() {
        ListEntity list = new ListEntity();
        list.setId(id);
        list.setOwner(owner);
        list.setTitle(title);
        list.setDescription(description);
        list.setDeleted(deleted);

        owner.getLists().add(list);

        if (owner == null) {
            throw new IllegalStateException("Owner must be set");
        }

        return list;
    }
}
