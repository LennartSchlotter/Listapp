package com.example.listapp.controller;

import static com.example.listapp.util.dtos.ListResponseDtoBuilder.aListResponseDto;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

import com.example.listapp.dto.list.ListCreateDto;
import com.example.listapp.dto.list.ListResponseDto;
import com.example.listapp.dto.list.ListUpdateDto;
import com.example.listapp.service.ListService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

@WebMvcTest(ListController.class)
public class ListControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ListService listService;

    ObjectMapper objectMapper = new ObjectMapper().registerModule(new Jdk8Module());

    @Test
    @WithMockUser
    void createList_ShouldReturnId_WhenAuthenticated() throws Exception {
        ListCreateDto dto = new ListCreateDto("Test List", "test");
        UUID expectedId = UUID.randomUUID();
        when(listService.createList(dto)).thenReturn(expectedId);

        mockMvc.perform(post("/api/v1/lists")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(expectedId.toString()));

        verify(listService).createList(dto);
    }

    @Test
    @WithMockUser
    void getListById_ShouldReturnListDetails_WhenAuthenticated() throws Exception {
        ListResponseDto responseDto = aListResponseDto().withId(UUID.randomUUID()).build();
        when(listService.getListById(responseDto.id())).thenReturn(responseDto);

        mockMvc.perform(get("/api/v1/lists/{id}", responseDto.id())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(responseDto.id().toString()));

        verify(listService).getListById(responseDto.id());
    }

    @Test
    @WithMockUser
    void getLists_ShouldReturnLists_WhenAuthenticated() throws Exception {
        ListResponseDto responseDto1 = aListResponseDto().withId(UUID.randomUUID()).build();
        ListResponseDto responseDto2 = aListResponseDto().withId(UUID.randomUUID()).build();
        List<ListResponseDto> listResponse = List.of(responseDto1, responseDto2);
        when(listService.getAllUserLists()).thenReturn(listResponse);

        mockMvc.perform(get("/api/v1/lists")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(responseDto1.id().toString()))
                .andExpect(jsonPath("$[1].id").value(responseDto2.id().toString()));

        verify(listService).getAllUserLists();
    }

    @Test
    @WithMockUser
    void updateList_ShouldReturnId_WhenAuthenticated() throws Exception {
        ListUpdateDto dto = new ListUpdateDto(Optional.of("Updated Name"), Optional.of("Updated List"));
        UUID randomId = UUID.randomUUID();
        when(listService.updateList(randomId, dto)).thenReturn(randomId);

        mockMvc.perform(patch("/api/v1/lists/{id}", randomId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(randomId.toString()));

        verify(listService).updateList(randomId, dto);
    }

    @Test
    @WithMockUser
    void deleteList_ShouldReturnNoContent_WhenAuthenticated() throws Exception {
        UUID randomId = UUID.randomUUID();
        mockMvc.perform(delete("/api/v1/lists/{id}", randomId)
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(listService).deleteList(randomId);
    }

    //Successful requests: Verify correct HTTP status (e.g., 200 OK or 201 Created), response body content, and headers.
    //Error scenarios: Test invalid inputs, validation failures (e.g., using @Valid), not-found cases (404), and server errors (500).
    //Different HTTP methods: GET, POST, PUT, DELETE, etc.
    //Path variables, query parameters, and request bodies: Ensure proper binding and handling.
    //Content negotiation: JSON serialization/deserialization, media types.
    //Security (if applicable): Test authentication/authorization with Spring Security test support.
    //Validation: Assert that bean validation annotations (e.g., @NotNull) trigger appropriate errors.
}
