package com.example.listapp.controller;

import static com.example.listapp.util.entities.ListTestBuilder.aList;
import static com.example.listapp.util.entities.ItemTestBuilder.anItem;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.listapp.dto.item.ItemCreateDto;
import com.example.listapp.dto.item.ItemReorderDto;
import com.example.listapp.dto.item.ItemUpdateDto;
import com.example.listapp.entity.Item;
import com.example.listapp.entity.ListEntity;
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
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(expectedId.toString()));

        verify(itemService).createItem(list.getId(), dto);
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
    void deleteItem_ShouldReturnNoContent_WhenAuthenticated() throws Exception {
        ListEntity list = aList().withId(UUID.randomUUID()).build();
        Item item = anItem().withId(UUID.randomUUID()).withList(list).build();

        mockMvc.perform(delete("/api/v1/lists/{listId}/items/{id}", list.getId(), item.getId())
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(itemService).deleteItem(list.getId(), item.getId());
    }
}
