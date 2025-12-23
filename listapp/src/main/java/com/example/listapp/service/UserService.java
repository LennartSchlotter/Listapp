package com.example.listapp.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.listapp.dto.user.UserResponseDto;
import com.example.listapp.dto.user.UserUpdateDto;
import com.example.listapp.entity.User;
import com.example.listapp.mapper.UserMapper;
import com.example.listapp.repository.UserRepository;
import com.example.listapp.security.SecurityUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    
    private final UserRepository _userRepository;
    private final UserMapper _userMapper;
    private final SecurityUtil _securityUtil;
    
    /**
     * Retrieves the currently authenticated user.
     * @return the currently authenticated user.
     */
    @Transactional(readOnly = true)
    public UserResponseDto getUser() {
        User user = _securityUtil.getCurrentUser();
        
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
        User entityToUpdate = _securityUtil.getCurrentUser();
        
        dto.name().ifPresent(entityToUpdate::setName);
        dto.email().ifPresent(entityToUpdate::setEmail);

        _userRepository.save(entityToUpdate);
        log.info("Updated user with id: {}", entityToUpdate.getId());

        return entityToUpdate.getId();
    }
    
    /**
     * Deletes the currently authenticated user.
     */
    @Transactional
    public void deleteUser() {
        User entity = _securityUtil.getCurrentUser();

        _userRepository.delete(entity);
        log.info("Deleted user with id: {}", entity.getId());
    }
}
