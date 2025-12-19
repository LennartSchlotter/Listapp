package com.example.listapp.service;

import java.util.UUID;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.listapp.dto.user.UserResponseDto;
import com.example.listapp.dto.user.UserUpdateDto;
import com.example.listapp.entity.User;
import com.example.listapp.mapper.UserMapper;
import com.example.listapp.repository.UserRepository;
import com.example.listapp.security.CustomOAuth2User;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository _userRepository;
    private final UserMapper _userMapper;
    
    /**
     * Retrieves the currently authenticated user.
     * @return the currently authenticated user.
     */
    @Transactional(readOnly = true)
    public UserResponseDto getUser() {
        User user = getCurrentUser();
        
        UserResponseDto response = _userMapper.toResponseDto(user);
        return response;
    }
    
    /**
     * Updates the currently authenticated user.
     * @param dto The to be changed values of the user.
     * @return The ID of the updated User.
     */
    @Transactional
    public UUID updateUser(UserUpdateDto dto) {
        User entityToUpdate = getCurrentUser();
        
        dto.name().ifPresent(entityToUpdate::setName);
        dto.email().ifPresent(entityToUpdate::setEmail);

        _userRepository.save(entityToUpdate);
        return entityToUpdate.getId();
    }
    
    /**
     * Deletes the currently authenticated user.
     */
    @Transactional
    public void deleteUser() {
        User entity = getCurrentUser();
        
        entity = _userRepository.findById(entity.getId())
        .orElseThrow(() -> new EntityNotFoundException("User not found"));

        _userRepository.delete(entity);
    }

    private User getCurrentUser() {
        CustomOAuth2User principal = (CustomOAuth2User) SecurityContextHolder
            .getContext()
            .getAuthentication()
            .getPrincipal();
        return principal.getUser();
    }
}
