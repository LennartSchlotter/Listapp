package com.example.listapp.controller;

import static com.example.listapp.util.dtos.ListResponseDtoBuilder.aListResponseDto;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

import com.example.listapp.dto.list.ListCreateDto;
import com.example.listapp.dto.list.ListResponseDto;
import com.example.listapp.dto.list.ListUpdateDto;
import com.example.listapp.exception.custom.ResourceAlreadyExistsException;
import com.example.listapp.exception.custom.ResourceNotFoundException;
import com.example.listapp.service.ListService;
import com.example.listapp.service.Security.ListSecurityService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@WebMvcTest(ListController.class)
public class ListControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ListService listService;

    @MockitoBean
    private ListSecurityService listSecurityService;

    ObjectMapper objectMapper = new ObjectMapper()
        .registerModule(new JavaTimeModule())
        .registerModule(new Jdk8Module())
        .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

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
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, Matchers.endsWith("/api/v1/lists/" + expectedId)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(expectedId.toString()));

        verify(listService).createList(dto);
    }

    @Test
    @WithMockUser
    void createList_ShouldThrowBadRequest_WhenInvalidTitle() throws Exception {
        String longTitle = "a".repeat(101);
        ListCreateDto invalidDto = new ListCreateDto(longTitle, "test");

        mockMvc.perform(post("/api/v1/lists")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidDto))
                .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value("Validation failed"));

        verifyNoInteractions(listService);
    }

    @Test
    @WithMockUser
    void createList_ShouldThrowBadRequest_WhenEmptyTitle() throws Exception {
        ListCreateDto invalidDto = new ListCreateDto(null, "test");

        mockMvc.perform(post("/api/v1/lists")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidDto))
                .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value("Validation failed"));

        verifyNoInteractions(listService);
    }

    @Test
    @WithMockUser
    void createList_ShouldThrowBadRequest_WhenInvalidDescription() throws Exception {
        String longDescription = "a".repeat(2001);
        ListCreateDto invalidDto = new ListCreateDto("Test", longDescription);

        mockMvc.perform(post("/api/v1/lists")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidDto))
                .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value("Validation failed"));

        verifyNoInteractions(listService);
    }

    @Test
    @WithMockUser
    void createList_ShouldThrowConflict_WhenResourceAlreadyExists() throws Exception {
        ListCreateDto dto = new ListCreateDto("Test List", "test");
        UUID expectedId = UUID.randomUUID();
        when(listService.createList(dto)).thenThrow(new ResourceAlreadyExistsException("List", expectedId.toString()));

        String expectedMessage = String.format("List already exists with identifier: %s", expectedId.toString());

        mockMvc.perform(post("/api/v1/lists")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .with(csrf()))
                .andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value(expectedMessage))
                .andExpect(jsonPath("$.errorCode").value("RESOURCE_ALREADY_EXISTS"))
                .andExpect(jsonPath("$.message").value("Conflict"));

        verify(listService).createList(dto);
    }

    @Test
    @WithMockUser
    void createList_ShouldThrowServerError_WhenUnexpectedServiceErrorOccurs() throws Exception {
        ListCreateDto dto = new ListCreateDto("Test List", "test");
        when(listService.createList(dto)).thenThrow(new RuntimeException("Unexpected error occured."));

        mockMvc.perform(post("/api/v1/lists")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .with(csrf()))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode").value("INTERNAL_SERVER_ERROR"))
                .andExpect(jsonPath("$.message").value("An unexpected error occured"));

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
                .andExpect(jsonPath("$.id").value(responseDto.id().toString()))
                .andExpect(content().json(objectMapper.writeValueAsString(responseDto)));

        verify(listService).getListById(responseDto.id());
    }

    @Test
    @WithMockUser
    void getListById_ShouldThrowNotFound_WhenNoElementWithIdExists() throws Exception {
        UUID randomId = UUID.randomUUID();
        when(listService.getListById(randomId)).thenThrow(new ResourceNotFoundException("List", randomId.toString()));

        String expectedMessage = String.format("List not found with identifier: %s", randomId.toString());

        mockMvc.perform(get("/api/v1/lists/{id}", randomId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value(expectedMessage))
                .andExpect(jsonPath("$.errorCode").value("RESOURCE_NOT_FOUND"));

        verify(listService, times(1)).getListById(randomId);
    }

    @Test
    @WithMockUser
    void getListById_ShouldThrowBadRequest_WhenInvalidQueryParameterIsPassed() throws Exception {
        String invalidId = "123";

        mockMvc.perform(get("/api/v1/lists/{id}", invalidId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value("Validation failed"));

        verifyNoInteractions(listService);
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
                .andExpect(jsonPath("$[1].id").value(responseDto2.id().toString()))
                .andExpect(jsonPath("$.size()").value(listResponse.size()))
                .andExpect(content().json(objectMapper.writeValueAsString(listResponse)));

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
    void updateList_ShouldThrowBadRequest_WhenInvalidTitle() throws Exception {
        String longTitle = "a".repeat(101);
        ListUpdateDto invalidDto = new ListUpdateDto(Optional.of(longTitle), Optional.empty());

        mockMvc.perform(patch("/api/v1/lists/{id}", UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidDto))
                .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value("Validation failed"));

        verifyNoInteractions(listService);
    }

    @Test
    @WithMockUser
    void updateList_ShouldThrowBadRequest_WhenInvalidDescription() throws Exception {
        String longDescription = "a".repeat(2001);
        ListUpdateDto invalidDto = new ListUpdateDto(null, Optional.of(longDescription));

        mockMvc.perform(patch("/api/v1/lists/{id}", UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidDto))
                .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value("Validation failed"));

        verifyNoInteractions(listService);
    }

    @Test
    @WithMockUser
    void updateList_ShouldThrowNotFound_WhenListNotFound() throws Exception {
        UUID randomId = UUID.randomUUID();
        ListUpdateDto dto = new ListUpdateDto(Optional.of("TitleUpdate"), Optional.empty());
        when(listService.updateList(randomId, dto)).thenThrow(new ResourceNotFoundException("List", randomId.toString()));

        String expectedMessage = String.format("List not found with identifier: %s", randomId.toString());

        mockMvc.perform(patch("/api/v1/lists/{id}", randomId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .with(csrf()))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value(expectedMessage))
                .andExpect(jsonPath("$.errorCode").value("RESOURCE_NOT_FOUND"));

        verify(listService, times(1)).updateList(randomId, dto);
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

    @Test
    @WithMockUser
    void deleteList_ShouldThrowNotFound_WhenNoElementWithIdExists() throws Exception {
        UUID randomId = UUID.randomUUID();
        doThrow(new ResourceNotFoundException("List", randomId.toString())).when(listService).deleteList(randomId);

        String expectedMessage = String.format("List not found with identifier: %s", randomId.toString());

        mockMvc.perform(delete("/api/v1/lists/{id}", randomId)
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value(expectedMessage))
                .andExpect(jsonPath("$.errorCode").value("RESOURCE_NOT_FOUND"));

        verify(listService, times(1)).deleteList(randomId);
    }

    @Test
    @WithMockUser
    void deleteList_ShouldThrowBadRequest_WhenInvalidQueryParameterIsPassed() throws Exception {
        String invalidId = "123";

        mockMvc.perform(delete("/api/v1/lists/{id}", invalidId)
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value("Validation failed"));

        verifyNoInteractions(listService);
    }
}
