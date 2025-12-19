package com.example.listapp.service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.listapp.dto.item.ItemCreateDto;
import com.example.listapp.dto.item.ItemReorderDto;
import com.example.listapp.dto.item.ItemUpdateDto;
import com.example.listapp.entity.Item;
import com.example.listapp.entity.ListEntity;
import com.example.listapp.mapper.ItemMapper;
import com.example.listapp.repository.ItemRepository;
import com.example.listapp.repository.ListRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository _itemRepository;
    private final ListRepository _listRepository;
    private final ItemMapper _itemMapper;

    /**
     * Creates a new item.
     * @param listId The ID of the list to create an item for.
     * @param dto The item to be created.
     * @return The ID of the created item.
     */
    @Transactional
    public UUID createItem(UUID listId, ItemCreateDto dto) {
        ListEntity list = _listRepository.findById(listId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "List not found"));

        Item createdEntity = _itemMapper.toEntity(dto);
        createdEntity.setList(list);

        int nextPosition = Math.max(0, (int) _itemRepository.countByListId(listId));
        createdEntity.setPosition(nextPosition);

        Item savedEntity = _itemRepository.save(createdEntity);
        return savedEntity.getId();
    }
    
    /**
     * Updates an existing item.
     * @param listId The ID of the list the item to be updated is associated to.
     * @param id The ID of the item to update.
     * @param dto The to be changed values of the item.
     * @return The ID of the updated item.
     */
    @Transactional
    public UUID updateItem(UUID listId, UUID id, ItemUpdateDto dto) {
        Item entityToUpdate = _itemRepository.findByIdAndListId(id, listId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "List not found with Id: " + id));
        
        dto.title().ifPresent(entityToUpdate::setTitle);
        dto.notes().ifPresent(entityToUpdate::setNotes);
        dto.imagePath().ifPresent(entityToUpdate::setImagePath);

        _itemRepository.save(entityToUpdate);
        return entityToUpdate.getId();
    }
    
    /**
     * Updates the order of items.
     * @param listId The ID of the list to perform reordering for.
     * @param dto The changed order.
     */
    @Transactional
    public void reorderItems(UUID listId, ItemReorderDto dto) {
        List<UUID> newOrder = dto.itemOrder();

        if (newOrder == null || newOrder.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Item order list cannot be empty");
        }

        ListEntity list = _listRepository.findById(listId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "List not found with Id: " + listId));

        Set<UUID> currentItemIds = list.getItems().stream()
            .map(Item::getId)
            .collect(Collectors.toSet());

        Set<UUID> requestedItemIds = new HashSet<>(newOrder);

        if (!currentItemIds.equals(requestedItemIds)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                "Provided item order does not match the current items in the list");
        }

        List<Item> items = _itemRepository.findAllByListIdOrderByPositionAsc(listId);

        Map<UUID, Item> itemById = items.stream()
            .collect(Collectors.toMap(Item::getId, Function.identity()));

        // Iterate through the list, changing position values as by the requested order
        for (int newPosition = 0; newPosition < newOrder.size(); newPosition++){
            UUID itemId = newOrder.get(newPosition);
            Item item = itemById.get(itemId);

            if (item == null){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid item ID in reorder request");
            }

            item.setPosition(newPosition);
        }

        _itemRepository.saveAll(items);
    }
    
    /**
     * Deletes an item.
     * @param listId The ID of the list the item to be deleted is associated to.
     * @param id The ID of teh item to be deleted.
     */
    @Transactional
    public void deleteItem(UUID listId, UUID id) {
        Item entity = _itemRepository.findByIdAndListId(id, listId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No Item found with id: " + id));
        
        entity.markAsDeleted();
        _itemRepository.save(entity);

        List<Item> remainingItems = _itemRepository.findAllByListIdOrderByPositionAsc(listId);
        for (int i = 0; i < remainingItems.size(); i++){
            Item item = remainingItems.get(i);
            if (item.getPosition() != i){
                item.setPosition(i);
            }
        }

        if (!remainingItems.isEmpty()) {
            _itemRepository.saveAll(remainingItems.stream()
                .filter(item -> item.getPosition() != remainingItems.indexOf(item))
                .toList());
        }

    }
}