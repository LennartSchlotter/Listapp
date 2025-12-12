package com.example.listapp.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.listapp.entity.Item;

public interface ItemRepository extends JpaRepository<Item, UUID>{

    /**
     * Retrieves all Items for a specified list, sorted ascending.
     * @param listId ID of the list to retrieve items for.
     * @return the sorted list of items.
     */
    List<Item> findAllByListIdOrderByPositionAsc(UUID listId);
    
    /**
     * Security method to find a specific item and verify that it belongs to the correct list.
     * @param itemId ID of the item to perform the check on.
     * @param listId ID of the list in question.
     * @return an item if the ID in question matches the passed list.
     */
    Optional<Item> findByIdAndListId(UUID itemId, UUID listId);
    
    /**
     * Bulk operation to retrieve the amount of items on a specifc list.
     * @param listId ID of the list to retrieve the number for.
     * @return an integer representing the amount of items.
     */
    long countByListId(UUID listId);
    
    /**
     * Deletes all items for a specified list.
     * @param listId ID of the list to delete all items for.
     */
    void deleteAllByListId(UUID listId);
}
