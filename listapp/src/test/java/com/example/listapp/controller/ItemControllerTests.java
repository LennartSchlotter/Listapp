package com.example.listapp.controller;

import static com.example.listapp.util.entities.ListTestBuilder.aList;
import static com.example.listapp.util.entities.ItemTestBuilder.anItem;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.listapp.dto.item.ItemCreateDto;
import com.example.listapp.dto.item.ItemReorderDto;
import com.example.listapp.dto.item.ItemUpdateDto;
import com.example.listapp.entity.Item;
import com.example.listapp.entity.ListEntity;
import com.example.listapp.exception.custom.ResourceAlreadyExistsException;
import com.example.listapp.exception.custom.ResourceNotFoundException;
import com.example.listapp.service.ItemService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

@WebMvcTest(ItemController.class)
public class ItemControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ItemService itemService;

    ObjectMapper objectMapper = new ObjectMapper().registerModule(new Jdk8Module());

    @Test
    @WithMockUser
    void createItem_ShouldReturnId_WhenAuthenticated() throws Exception {
        ListEntity list = aList().withId(UUID.randomUUID()).build();

        ItemCreateDto dto = new ItemCreateDto("Testitem", "Test notes", null);
        UUID expectedId = UUID.randomUUID();
        when(itemService.createItem(list.getId(), dto)).thenReturn(expectedId);

        mockMvc.perform(post("/api/v1/lists/{listId}/items", list.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, Matchers.endsWith("/api/v1/lists/" + list.getId() + "/items/" + expectedId)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(expectedId.toString()));

        verify(itemService).createItem(list.getId(), dto);
    }

    @Test
    @WithMockUser
    void createItem_ShouldThrowBadRequest_WhenInvalidTitle() throws Exception {
        ListEntity list = aList().withId(UUID.randomUUID()).build();
        
        String longTitle = "a".repeat(101);
        ItemCreateDto dto = new ItemCreateDto(longTitle, "Test notes", null);

        mockMvc.perform(post("/api/v1/lists/{listId}/items", list.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value("Validation failed"));

        verifyNoInteractions(itemService);
    }

    @Test
    @WithMockUser
    void createItem_ShouldThrowBadRequest_WhenInvalidNotes() throws Exception {
        ListEntity list = aList().withId(UUID.randomUUID()).build();
        
        String longNotes = "a".repeat(2001);
        ItemCreateDto dto = new ItemCreateDto("Test", longNotes, null);

        mockMvc.perform(post("/api/v1/lists/{listId}/items", list.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value("Validation failed"));

        verifyNoInteractions(itemService);
    }


    @Test
    @WithMockUser
    void createItem_ShouldThrowBadRequest_WhenInvalidImagePath() throws Exception {
        ListEntity list = aList().withId(UUID.randomUUID()).build();
        
        String longImagePath = "a".repeat(1025);
        ItemCreateDto dto = new ItemCreateDto("Test", "Test notes", longImagePath);

        mockMvc.perform(post("/api/v1/lists/{listId}/items", list.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value("Validation failed"));

        verifyNoInteractions(itemService);
    }

    @Test
    @WithMockUser
    void createItem_ShouldThrowBadRequest_WhenNullTitle() throws Exception {
        ListEntity list = aList().withId(UUID.randomUUID()).build();
        
        ItemCreateDto dto = new ItemCreateDto(null, "Test notes", null);

        mockMvc.perform(post("/api/v1/lists/{listId}/items", list.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value("Validation failed"));

        verifyNoInteractions(itemService);
    }

    @Test
    @WithMockUser
    void createItem_ShouldThrowConflict_WhenResourceAlreadyExists() throws Exception {
        ItemCreateDto dto = new ItemCreateDto("Test Item", "test", null);
        UUID listId = UUID.randomUUID();
        UUID expectedId = UUID.randomUUID();
        when(itemService.createItem(listId, dto)).thenThrow(new ResourceAlreadyExistsException("Item", expectedId.toString()));

        String expectedMessage = String.format("Item already exists with identifier: %s", expectedId.toString());

        mockMvc.perform(post("/api/v1/lists/{listId}/items", listId, expectedId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .with(csrf()))
                .andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value(expectedMessage))
                .andExpect(jsonPath("$.errorCode").value("RESOURCE_ALREADY_EXISTS"))
                .andExpect(jsonPath("$.message").value("Conflict"));

        verify(itemService).createItem(listId, dto);
    }

    @Test
    @WithMockUser
    void createItem_ShouldThrowServerError_WhenUnexpectedServiceErrorOccurs() throws Exception {
        ItemCreateDto dto = new ItemCreateDto("Test Item", "test", null);
        UUID listId = UUID.randomUUID();
        UUID expectedId = UUID.randomUUID();
        when(itemService.createItem(listId, dto)).thenThrow(new RuntimeException("Unexpected error occured."));

        mockMvc.perform(post("/api/v1/lists/{listId}/items", listId, expectedId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .with(csrf()))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode").value("INTERNAL_SERVER_ERROR"))
                .andExpect(jsonPath("$.message").value("An unexpected error occured"));

        verify(itemService).createItem(listId, dto);
    }

    @Test
    @WithMockUser
    void updateItem_ShouldReturnId_WhenAuthenticated() throws Exception {
        ListEntity list = aList().withId(UUID.randomUUID()).build();

        ItemUpdateDto dto = new ItemUpdateDto(Optional.of("Updated Item"), Optional.of("Test notes"), Optional.empty());
        UUID expectedId = UUID.randomUUID();
        when(itemService.updateItem(list.getId(), expectedId, dto)).thenReturn(expectedId);

        mockMvc.perform(patch("/api/v1/lists/{listId}/items/{id}", list.getId(), expectedId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(expectedId.toString()));

        verify(itemService).updateItem(list.getId(), expectedId, dto);
    }

    @Test
    @WithMockUser
    void updateItem_ShouldThrowBadRequest_WhenInvalidTitle() throws Exception {
        ListEntity list = aList().withId(UUID.randomUUID()).build();
        
        String longTitle = "a".repeat(101);
        ItemUpdateDto dto = new ItemUpdateDto(Optional.of(longTitle), Optional.empty(), Optional.empty());

        mockMvc.perform(patch("/api/v1/lists/{listId}/items/{id}", list.getId(), UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value("Validation failed"));

        verifyNoInteractions(itemService);
    }

    @Test
    @WithMockUser
    void updateItem_ShouldThrowBadRequest_WhenInvalidNotes() throws Exception {
        ListEntity list = aList().withId(UUID.randomUUID()).build();
        
        String longNotes = "a".repeat(2001);
        ItemUpdateDto dto = new ItemUpdateDto(Optional.empty(), Optional.of(longNotes), Optional.empty());

        mockMvc.perform(patch("/api/v1/lists/{listId}/items/{id}", list.getId(), UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value("Validation failed"));

        verifyNoInteractions(itemService);
    }


    @Test
    @WithMockUser
    void updateItem_ShouldThrowBadRequest_WhenInvalidImagePath() throws Exception {
        ListEntity list = aList().withId(UUID.randomUUID()).build();
        
        String longImagePath = "a".repeat(1025);
        ItemUpdateDto dto = new ItemUpdateDto(Optional.empty(), Optional.empty(), Optional.of(longImagePath));

        mockMvc.perform(patch("/api/v1/lists/{listId}/items/{id}", list.getId(), UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value("Validation failed"));

        verifyNoInteractions(itemService);
    }

    @Test
    @WithMockUser
    void updateItem_ShouldThrowNotFound_WhenItemNotFound() throws Exception {
        ListEntity list = aList().withId(UUID.randomUUID()).build();

        UUID randomId = UUID.randomUUID();
        ItemUpdateDto dto = new ItemUpdateDto(Optional.of("UpdateItemTitle"), Optional.empty(), Optional.empty());
        when(itemService.updateItem(list.getId(), randomId, dto)).thenThrow(new ResourceNotFoundException("Item", randomId.toString()));

        String expectedMessage = String.format("Item not found with identifier: %s", randomId.toString());

        mockMvc.perform(patch("/api/v1/lists/{listId}/items/{id}", list.getId(), randomId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .with(csrf()))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value(expectedMessage))
                .andExpect(jsonPath("$.errorCode").value("RESOURCE_NOT_FOUND"));

        verify(itemService, times(1)).updateItem(list.getId(), randomId, dto);
    }

    @Test
    @WithMockUser
    void reorderItems_ShouldReturnNoContent_WhenAuthenticated() throws Exception {
        ListEntity list = aList().withId(UUID.randomUUID()).build();
        Item item1 = anItem().withId(UUID.randomUUID()).withList(list).build();
        Item item2 = anItem().withId(UUID.randomUUID()).withList(list).build();
        ItemReorderDto dto = new ItemReorderDto(List.of(item1.getId(), item2.getId()));

        mockMvc.perform(patch("/api/v1/lists/{listId}/items/order", list.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .with(csrf()))
                .andExpect(status().isNoContent());

        verify(itemService).reorderItems(list.getId(), dto);
    }

    @Test
    @WithMockUser
    void reorderItems_ShouldThrowNotFound_WhenNotExistingItemPassed() throws Exception {
        ListEntity list = aList().withId(UUID.randomUUID()).build();
        Item item1 = anItem().withId(UUID.randomUUID()).withList(list).build();
        UUID randomId = UUID.randomUUID();
        ItemReorderDto dto = new ItemReorderDto(List.of(item1.getId(), randomId));
        doThrow(new ResourceNotFoundException("Item", randomId.toString())).when(itemService).reorderItems(list.getId(), dto);

        String expectedMessage = String.format("Item not found with identifier: %s", randomId.toString());

        mockMvc.perform(patch("/api/v1/lists/{listId}/items/order", list.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .with(csrf()))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value(expectedMessage))
                .andExpect(jsonPath("$.errorCode").value("RESOURCE_NOT_FOUND"));

        verify(itemService).reorderItems(list.getId(), dto);
    }

    @Test
    @WithMockUser
    void deleteItem_ShouldReturnNoContent_WhenAuthenticated() throws Exception {
        ListEntity list = aList().withId(UUID.randomUUID()).build();
        Item item = anItem().withId(UUID.randomUUID()).withList(list).build();

        mockMvc.perform(delete("/api/v1/lists/{listId}/items/{id}", list.getId(), item.getId())
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(itemService).deleteItem(list.getId(), item.getId());
    }

    @Test
    @WithMockUser
    void deleteItem_ShouldThrowNotFound_WhenNoElementWithIdExist() throws Exception {
        ListEntity list = aList().withId(UUID.randomUUID()).build();
        UUID randomId = UUID.randomUUID();
        doThrow(new ResourceNotFoundException("Item", randomId.toString())).when(itemService).deleteItem(list.getId(), randomId);

        String expectedMessage = String.format("Item not found with identifier: %s", randomId.toString());

        mockMvc.perform(delete("/api/v1/lists/{listId}/items/{id}", list.getId(), randomId)
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value(expectedMessage))
                .andExpect(jsonPath("$.errorCode").value("RESOURCE_NOT_FOUND"));

        verify(itemService, times(1)).deleteItem(list.getId(), randomId);;
    }

    @Test
    @WithMockUser
    void deleteItem_ShouldThrowBadRequest_WhenInvalidQueryParameterIsPassed() throws Exception {
        ListEntity list = aList().withId(UUID.randomUUID()).build();
        String invalidId = "123";

        mockMvc.perform(delete("/api/v1/lists/{listId}/items/{id}", list.getId(), invalidId)
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value("Validation failed"));

        verifyNoInteractions(itemService);
    }
}
