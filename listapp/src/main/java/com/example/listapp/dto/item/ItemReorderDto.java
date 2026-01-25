package com.example.listapp.dto.item;

import java.util.List;
import java.util.UUID;

public record ItemReorderDto(List<UUID> itemOrder) {

    /**
     * Copies the itemOrder list as immutability guarantee.
     * @param itemOrder the order of items.
     */
    public ItemReorderDto {
        itemOrder = List.copyOf(itemOrder);
    }
}
