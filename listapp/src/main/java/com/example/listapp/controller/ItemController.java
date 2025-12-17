package com.example.listapp.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.listapp.dto.item.ItemCreateDto;
import com.example.listapp.dto.item.ItemReorderDto;
import com.example.listapp.dto.item.ItemUpdateDto;
import com.example.listapp.service.ItemService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/lists/{listId}/items")
public class ItemController {

    private final ItemService _itemService;

    public ItemController(ItemService itemService){
        _itemService = itemService;
    }

    @PostMapping
    @PreAuthorize("@itemSecurity.canAccessList(#listId)")
    public ResponseEntity<UUID> CreateItem(@PathVariable UUID listId, @Valid @RequestBody ItemCreateDto dto){
        UUID createdId = _itemService.createItem(listId, dto);
        return ResponseEntity.ok(createdId);
    }

    @PatchMapping("/{id}")
    @ResponseBody
    @PreAuthorize("@itemSecurity.canAccessItem(#listId, #id)")
    public ResponseEntity<UUID> UpdateItem(@PathVariable UUID listId, @PathVariable UUID id, @Valid @RequestBody ItemUpdateDto dto){
        UUID updatedId = _itemService.updateItem(listId, id, dto);
        return ResponseEntity.ok(updatedId);
    }

    @PatchMapping("/order")
    @ResponseBody
    @PreAuthorize("@itemSecurity.canAccessList(#listId)")
    public ResponseEntity<Void> ReorderItems(@PathVariable UUID listId, @Valid @RequestBody ItemReorderDto dto){
        _itemService.reorderItems(listId, dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    @PreAuthorize("@itemSecurity.canAccessItem(#listId, #id)")
    public ResponseEntity<Void> DeleteItem(@PathVariable UUID listId, @PathVariable UUID id){
        _itemService.deleteItem(listId, id);
        return ResponseEntity.noContent().build();
    }
}
