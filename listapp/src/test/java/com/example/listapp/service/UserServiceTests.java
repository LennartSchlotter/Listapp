package com.example.listapp.service;

import static com.example.listapp.util.dtos.UserResponseDtoBuilder.aUserResponseDto;
import static com.example.listapp.util.entities.UserTestBuilder.aUser;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;

import com.example.listapp.dto.user.UserResponseDto;
import com.example.listapp.dto.user.UserUpdateDto;
import com.example.listapp.entity.User;
import com.example.listapp.exception.custom.AccessDeniedException;
import com.example.listapp.mapper.UserMapper;
import com.example.listapp.repository.UserRepository;
import com.example.listapp.security.SecurityUtil;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {
    
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Mock
    UserMapper userMapper;

    @Mock
    SecurityUtil securityUtil;

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void getUser_shouldReturnDto_whenUserIsAuthenticated() {
        User user = aUser().build();
        UserResponseDto dto = aUserResponseDto()
            .withId(user.getId()).withName(user.getName()).withEmail(user.getEmail()).withCreatedAt(user.getCreatedAt()).withUpdatedAt(user.getUpdatedAt()).build();

        when(securityUtil.getCurrentUser()).thenReturn(user);
        when(userMapper.toResponseDto(user)).thenReturn(dto);

        UserResponseDto result = userService.getUser();

        assertEquals(dto, result);
        verify(userMapper).toResponseDto(user);
        verifyNoInteractions(userRepository);
    }

    @Test
    void getUser_shouldThrowException_whenNoAuthentication() {
        when(securityUtil.getCurrentUser())
            .thenThrow(new AccessDeniedException("No authenticated user found"));

        assertThrows(AccessDeniedException.class, () -> userService.getUser());
        verifyNoInteractions(userMapper, userRepository);
    }

    @Test
    void updateUser_shouldReturnUpdatedDto_whenUserIsAuthenticated() {
        User user = aUser().withName("name").withEmail("email@mail.com").build();
        UserUpdateDto dto = new UserUpdateDto(Optional.empty(), Optional.of("tester"), Optional.of("tester@mail.com"));

        when(securityUtil.getCurrentUser()).thenReturn(user);

        UUID result = userService.updateUser(dto);

        assertEquals(user.getId(), result);
        assertEquals("tester", user.getName());
        assertEquals("tester@mail.com", user.getEmail());

        verify(userRepository).save(user);
    }

    @Test
    void updateUser_shouldCallSave_whenNoChangesSubmitted() {
        User user = aUser().withName("name").withEmail("email@mail.com").build();
        UserUpdateDto dto = new UserUpdateDto(java.util.Optional.empty(), java.util.Optional.empty(), java.util.Optional.empty());

        when(securityUtil.getCurrentUser()).thenReturn(user);

        UUID result = userService.updateUser(dto);

        assertEquals(user.getId(), result);
        verify(userRepository).save(user);
    }

    @Test
    void updateUser_shouldThrowException_whenNoAuthentication() {
        aUser().withName("name").withEmail("email@mail.com").build();
        UserUpdateDto dto = new UserUpdateDto(Optional.empty(), Optional.of("tester"), Optional.of("tester@mail.com"));

        when(securityUtil.getCurrentUser())
            .thenThrow(new AccessDeniedException("No authenticated user found"));

        assertThrows(AccessDeniedException.class, () -> userService.updateUser(dto));
    }

    @Test
    void deleteUser_shouldDeleteUser_whenUserIsAuthenticated() {
        User user = aUser().withName("name").withEmail("email@mail.com").build();

        when(securityUtil.getCurrentUser()).thenReturn(user);

        userService.deleteUser();

        verify(userRepository).delete(user);
        verifyNoInteractions(userMapper);
    }

    @Test
    void deleteUser_shouldThrowException_whenNoAuthentication() {
        when(securityUtil.getCurrentUser())
            .thenThrow(new AccessDeniedException("No authenticated user found"));

        assertThrows(AccessDeniedException.class, () -> userService.deleteUser());
    }
}
