package com.example.listapp.controller;

import static com.example.listapp.util.dtos.UserResponseDtoBuilder.aUserResponseDto;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.listapp.dto.user.UserResponseDto;
import com.example.listapp.dto.user.UserUpdateDto;
import com.example.listapp.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

@WebMvcTest(UserController.class)
public class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    ObjectMapper objectMapper = new ObjectMapper().registerModule(new Jdk8Module());

    @Test
    @WithMockUser
    void getUser_shouldReturnUserDetails_WhanAuthenticated() throws Exception {
        UserResponseDto responseDto = aUserResponseDto().withId(UUID.randomUUID()).build();
        when(userService.getUser()).thenReturn(responseDto);

        mockMvc.perform(get("/api/v1/user")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(responseDto.id().toString()));

        verify(userService).getUser();
    }

    @Test
    @WithMockUser
    void getUser_shouldReturnInternalServerError_whenServiceThrowsUnexpectedException() throws Exception {
        when(userService.getUser())
            .thenThrow(new RuntimeException("Unexpected database failure"));

        mockMvc.perform(get("/api/v1/user")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isInternalServerError())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.status").value(500))
            .andExpect(jsonPath("$.error").value("Internal Server Error"))
            .andExpect(jsonPath("$.errorCode").value("INTERNAL_SERVER_ERROR"));
    }

    @Test
    @WithMockUser
    void updateUser_shouldReturnOkAndId_whenAuthenticated() throws Exception {
        UUID randomId = UUID.randomUUID();
        UserUpdateDto updateDto = new UserUpdateDto(Optional.empty(), Optional.of("test"), Optional.of("test@gmail.com"));
        when(userService.updateUser(updateDto)).thenReturn(randomId);

        mockMvc.perform(patch("/api/v1/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(randomId.toString()));

        verify(userService).updateUser(any(UserUpdateDto.class));
    }

    @Test
    @WithMockUser
    void updateUser_shouldReturnBadRequest_whenInvalidEmail() throws Exception {
        UserUpdateDto invalidDto = new UserUpdateDto(Optional.empty(), Optional.empty(), Optional.of("invalid-email"));

        mockMvc.perform(patch("/api/v1/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidDto))
                .with(csrf()))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"))
            .andExpect(jsonPath("$.message").value("Validation failed"));

        verifyNoInteractions(userService);
    }

    @Test
    @WithMockUser
    void updateUser_shouldReturnBadRequest_whenInvalidName() throws Exception {
        String longName = "a".repeat(65);
        UserUpdateDto invalidDto = new UserUpdateDto(Optional.empty(), Optional.of(longName), Optional.empty());

        mockMvc.perform(patch("/api/v1/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidDto))
                .with(csrf()))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"))
            .andExpect(jsonPath("$.message").value("Validation failed"));

        verifyNoInteractions(userService);
    }

    @Test
    @WithMockUser
    void deleteUser_shouldReturnNoContent_whenAuthenticated() throws Exception {
        mockMvc.perform(delete("/api/v1/user")
                .with(csrf()))
                .andExpect(status().isNoContent());

        verify(userService).deleteUser();
    }
}
