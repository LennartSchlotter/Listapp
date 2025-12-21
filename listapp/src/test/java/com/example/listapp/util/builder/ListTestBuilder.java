package com.example.listapp.util.builder;

import java.util.UUID;

import com.example.listapp.entity.ListEntity;
import com.example.listapp.entity.User;

import static com.example.listapp.util.builder.UserTestBuilder.aUser;

public class ListTestBuilder {
    private UUID id = UUID.randomUUID();
    private User owner = aUser().build();
    private String title = "Test list";
    private String description = "Test description";
    private boolean deleted = false;

    public static ListTestBuilder aList() {
        return new ListTestBuilder();
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

        return list;
    }
}
