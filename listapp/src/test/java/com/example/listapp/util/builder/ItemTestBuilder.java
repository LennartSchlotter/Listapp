package com.example.listapp.util.builder;

import static com.example.listapp.util.builder.ListTestBuilder.aList;

import java.util.UUID;

import com.example.listapp.entity.Item;
import com.example.listapp.entity.ListEntity;

public class ItemTestBuilder {
    private UUID id = UUID.randomUUID();
    private ListEntity list = aList().build();
    private int position = 0;
    private String title = "Test item";
    private String notes = "Test notes";
    private String imagePath = null;
    private boolean deleted = false;

    public static ItemTestBuilder anItem() {
        return new ItemTestBuilder();
    }

    public ItemTestBuilder withList(ListEntity list) {
        this.list = list;
        return this;
    }

    public ItemTestBuilder withPosition(int position) {
        this.position = position;
        return this;
    }

    public ItemTestBuilder deleted() {
        this.deleted = true;
        return this;
    }

    public Item build() {
        Item item = new Item();
        item.setId(id);
        item.setList(list);
        item.setPosition(position);
        item.setTitle(title);
        item.setNotes(notes);
        item.setImagePath(imagePath);
        item.setDeleted(deleted);

        list.getItems().add(item);

        return item;
    }
}
