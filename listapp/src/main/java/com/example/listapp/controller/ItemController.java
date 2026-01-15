package com.example.listapp.controller;

import java.net.URI;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.listapp.dto.item.ItemCreateDto;
import com.example.listapp.dto.item.ItemReorderDto;
import com.example.listapp.dto.item.ItemUpdateDto;
import com.example.listapp.service.ItemService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.bind.annotation.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/lists/{listId}/items")
@Tag(name = "Item Controller", description =
    "APIs for managing items as part of lists")
public class ItemController {

    /**
     * Service for the item business logic.
     */
    private final ItemService itemService;

    /**
     * Constructor for the ItemController.
     * @param itemServiceParam service responsible for item business logic
     */
    public ItemController(final ItemService itemServiceParam) {
        this.itemService = itemServiceParam;
    }

    /**
     * Creates a new Item with the given data sourced from the request body.
     * @param listId The id of the List to create an item for.
     * @param dto The request body containing the data for the item.
     * @return An API response representing the result of the call.
     */
    @Operation(summary = "Create a new item", description =
        "Add a new item to the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description =
            "Item created successfully",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = UUID.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request data",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ProblemDetail.class))),
    })
    @PostMapping
    @PreAuthorize("@itemSecurity.canAccessList(#listId)")
    public ResponseEntity<UUID> createItem(
            final @Parameter(description = "ID of the list to add the item to")
            @PathVariable UUID listId,
            final @Valid @RequestBody ItemCreateDto dto) {
        UUID createdId = itemService.createItem(listId, dto);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().
            path("/{id}").buildAndExpand(createdId).toUri();
        return ResponseEntity.created(location).body(createdId);
    }

    /**
     * Updates a given Item with the given data sourced from the request body.
     * @param listId The id of the list the item to be updated belongs to.
     * @param id The id of the item to be updated.
     * @param dto The request body containing the data for the item.
     * @return An API response representing the result of the call.
     */
    @Operation(summary = "Update an item", description =
        "Update an existing item's details")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description =
            "Item updated successfully",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = UUID.class))),
        @ApiResponse(responseCode = "400", description =
            "Invalid request data",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "403", description = "Forbidden"),
        @ApiResponse(responseCode = "404", description =
            "List or item not found")
    })
    @PatchMapping("/{id}")
    @ResponseBody
    @PreAuthorize("@itemSecurity.canAccessItem(#listId, #id)")
    public ResponseEntity<UUID> updateItem(
            final @Parameter(description = "ID of the list containing the item")
            @PathVariable UUID listId,
            final @Parameter(description = "ID of the item to update")
            @PathVariable UUID id,
            final @Valid @RequestBody ItemUpdateDto dto) {
        UUID updatedId = itemService.updateItem(listId, id, dto);
        return ResponseEntity.ok(updatedId);
    }

    /**
     * Reorders the items in a specific list.
     * @param listId The id of the List to reorder items for.
     * @param dto The request body containing the ordered list of items.
     * @return An API response representing the result of the call.
     */
    @Operation(summary = "Reorder items in a list", description =
        "Reorder all items within the specified list")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description =
            "Items reordered successfully"),
        @ApiResponse(responseCode = "400", description =
            "Invalid reorder data",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "403", description = "Forbidden"),
        @ApiResponse(responseCode = "404", description = "List not found")
    })
    @PatchMapping("/order")
    @ResponseBody
    @PreAuthorize("@itemSecurity.canAccessList(#listId)")
    public ResponseEntity<Void> reorderItems(
            final @Parameter(description = "ID of the list to reorder items in")
            @PathVariable UUID listId,
            final @Valid @RequestBody ItemReorderDto dto) {
        itemService.reorderItems(listId, dto);
        return ResponseEntity.noContent().build();
    }

    /**
     * Deletes a given Item.
     * @param listId The id of the list the item to be deleted belongs to.
     * @param id The id of the item to be deleted.
     * @return An API response representing the result of the call.
     */
    @Operation(summary = "Delete an item", description =
        "Delete a specific item from the list")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description =
            "Item deleted successfully"),
        @ApiResponse(responseCode = "403", description =
            "Forbidden (insufficient permissions)"),
        @ApiResponse(responseCode = "404", description =
            "List or item not found")
    })
    @DeleteMapping("/{id}")
    @ResponseBody
    @PreAuthorize("@itemSecurity.canAccessItem(#listId, #id)")
    public ResponseEntity<Void> deleteItem(
            final @Parameter(description = "ID of the list containing the item")
            @PathVariable UUID listId,
            final @Parameter(description = "ID of the item to delete")
            @PathVariable UUID id) {
        itemService.deleteItem(listId, id);
        return ResponseEntity.noContent().build();
    }
}
