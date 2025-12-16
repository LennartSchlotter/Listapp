package com.example.listapp.service;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.listapp.dto.user.UserResponseDto;
import com.example.listapp.dto.user.UserUpdateDto;
import com.example.listapp.entity.User;
import com.example.listapp.mapper.UserMapper;
import com.example.listapp.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository _userRepository;
    private final UserMapper _userMapper;
    
    public UserResponseDto getUser() {
        String username = null;
        //Extract username from authentication principal
        User user = _userRepository.findByName(username)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No user found with the name: " + username));
        
        UserResponseDto response = _userMapper.toResponseDto(user);
        return response;
    }
    
    public UUID updateUser(UserUpdateDto dto) {
        String username = null;
        //Extract username from authentication principal
        User entityToUpdate = _userRepository.findByName(username)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No user found with the name: " + username));
        
        dto.oauth2_provider().ifPresent(entityToUpdate::setOauth2_provider);
        dto.name().ifPresent(entityToUpdate::setName);
        dto.email().ifPresent(entityToUpdate::setEmail);

        _userRepository.save(entityToUpdate);
        return entityToUpdate.getId();
    }
    
    public void deleteUser() {
        String username = null;
        //Extract username from authentication principal
        User entity = _userRepository.findByName(username)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No user found with the name: " + username));
        
        entity.markAsDeleted();
        _userRepository.save(entity);
    }
}
