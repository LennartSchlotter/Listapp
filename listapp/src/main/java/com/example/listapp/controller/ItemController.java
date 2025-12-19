package com.example.listapp.controller;

import java.util.UUID;

import org.springframework.http.ProblemDetail;
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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/lists/{listId}/items")
@Tag(name = "Item Controller", description = "APIs for managing items as part of lists")
public class ItemController {

    private final ItemService _itemService;

    public ItemController(ItemService itemService){
        _itemService = itemService;
    }

    @Operation(summary = "Create a new item", description = "Add a new item to the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Item created successfully",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = UUID.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request data",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ProblemDetail.class))),
    })
    @PostMapping
    @PreAuthorize("@itemSecurity.canAccessList(#listId)")
    public ResponseEntity<UUID> CreateItem(
            @Parameter(description = "ID of the list to add the item to") @PathVariable UUID listId, 
            @Valid @RequestBody ItemCreateDto dto) {
        UUID createdId = _itemService.createItem(listId, dto);
        return ResponseEntity.ok(createdId);
    }

    @Operation(summary = "Update an item", description = "Update an existing item's details")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Item updated successfully",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = UUID.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request data",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "403", description = "Forbidden"),
        @ApiResponse(responseCode = "404", description = "List or item not found")
    })
    @PatchMapping("/{id}")
    @ResponseBody
    @PreAuthorize("@itemSecurity.canAccessItem(#listId, #id)")
    public ResponseEntity<UUID> UpdateItem(
            @Parameter(description = "ID of the list containing the item") @PathVariable UUID listId, 
            @Parameter(description = "ID of the item to update") @PathVariable UUID id, 
            @Valid @RequestBody ItemUpdateDto dto) {
        UUID updatedId = _itemService.updateItem(listId, id, dto);
        return ResponseEntity.ok(updatedId);
    }

    @Operation(summary = "Reorder items in a list", description = "Reorder all items within the specified list")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Items reordered successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid reorder data",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "403", description = "Forbidden"),
        @ApiResponse(responseCode = "404", description = "List not found")
    })
    @PatchMapping("/order")
    @ResponseBody
    @PreAuthorize("@itemSecurity.canAccessList(#listId)")
    public ResponseEntity<Void> ReorderItems(
            @Parameter(description = "ID of the list to reorder items in") @PathVariable UUID listId, 
            @Valid @RequestBody ItemReorderDto dto) {
        _itemService.reorderItems(listId, dto);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete an item", description = "Delete a specific item from the list")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Item deleted successfully"),
        @ApiResponse(responseCode = "403", description = "Forbidden (insufficient permissions)"),
        @ApiResponse(responseCode = "404", description = "List or item not found")
    })
    @DeleteMapping("/{id}")
    @ResponseBody
    @PreAuthorize("@itemSecurity.canAccessItem(#listId, #id)")
    public ResponseEntity<Void> DeleteItem(
            @Parameter(description = "ID of the list containing the item") @PathVariable UUID listId, 
            @Parameter(description = "ID of the item to delete") @PathVariable UUID id){
        _itemService.deleteItem(listId, id);
        return ResponseEntity.noContent().build();
    }
}
