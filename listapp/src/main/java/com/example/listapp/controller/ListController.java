package com.example.listapp.controller;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.listapp.dto.list.ListCreateDto;
import com.example.listapp.dto.list.ListResponseDto;
import com.example.listapp.dto.list.ListUpdateDto;
import com.example.listapp.service.ListService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/lists")
@Tag(name = "List Controller", description =
    "APIs for managing user-owned lists")
public class ListController {

    /**
     * Service for the list business logic.
     */
    private final ListService listService;

    /**
     * Constructor for the ListController.
     * @param listServiceParam service responsible for list business logic
     */
    public ListController(final ListService listServiceParam) {
        this.listService = listServiceParam;
    }

    /**
     * Creates a new List with the given data sourced from the request body.
     * @param dto The request body containing the data for the list.
     * @return An API response representing the result of the call.
     */
    @Operation(summary = "Create a new list", description =
        "Add a new list to the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description =
            "List created successfully",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = UUID.class))),
        @ApiResponse(responseCode = "400", description =
            "Invalid request data",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PostMapping
    public ResponseEntity<UUID> createList(
        final @Valid @RequestBody ListCreateDto dto) {
        UUID createdId = listService.createList(dto);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}").buildAndExpand(createdId).toUri();
        return ResponseEntity.created(location).body(createdId);
    }

    /**
     * Retrieves a given list by its id.
     * @param id The id of the list to retrieve.
     * @return An API response representing the result of the call.
     */
    @Operation(summary = "Get a list by ID", description =
        "Retrieves details of a specific list")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description =
            "List retrieved successfully",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ListResponseDto.class))),
        @ApiResponse(responseCode = "403", description = "Forbidden"),
        @ApiResponse(responseCode = "404", description = "List not found")
    })
    @GetMapping("/{id}")
    @PreAuthorize("@listSecurity.isOwner(#id)")
    public ResponseEntity<ListResponseDto> getListById(
        final @Parameter(description = "ID of the list to retrieve")
        @PathVariable UUID id) {
        ListResponseDto response = listService.getListById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves all lists belonging to the current user.
     * @return An API response representing the result of the call.
     */
    @Operation(summary = "Get all lists", description =
        "Retrieves all lists of a specific user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description =
            "Lists retrieved successfully",
            content = @Content(mediaType = "application/json",
                array = @ArraySchema(
                schema = @Schema(type = "array",
                    implementation = ListResponseDto.class))))
    })
    @GetMapping
    public ResponseEntity<List<ListResponseDto>> getLists() {
        List<ListResponseDto> lists = listService.getAllUserLists();
        return ResponseEntity.ok(lists);
    }

    /**
     * Updates a given List with the given data sourced from the request body.
     * @param id The id of the list to be updated.
     * @param dto The request body containing the data for the list.
     * @return An API response representing the result of the call.
     */
    @Operation(summary = "Update a list", description =
        "Updates details of an existing list")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description =
            "List updated successfully",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = UUID.class))),
        @ApiResponse(responseCode = "400", description =
            "Invalid request data",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "403", description = "Forbidden"),
        @ApiResponse(responseCode = "404", description = "List not found")
    })
    @PatchMapping("/{id}")
    @PreAuthorize("@listSecurity.isOwner(#id)")
    public ResponseEntity<UUID> updateList(
        final @Parameter(description = "ID of the list to update")
        @PathVariable UUID id,
        final @Valid @RequestBody ListUpdateDto dto) {
        UUID updatedId = listService.updateList(id, dto);
        return ResponseEntity.ok(updatedId);
    }

    /**
     * Deletes a specified list.
     * @param id The id of the list to be deleted.
     * @return An API response representing the result of the call.
     */
    @Operation(summary = "Delete a list", description =
        "Deletes a list and all its associated items")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description =
            "List deleted successfully"),
        @ApiResponse(responseCode = "403", description = "Forbidden"),
        @ApiResponse(responseCode = "404", description = "List not found")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("@listSecurity.isOwner(#id)")
    public ResponseEntity<Void> deleteList(final @PathVariable UUID id) {
        listService.deleteList(id);
        return ResponseEntity.noContent().build();
    }
}
