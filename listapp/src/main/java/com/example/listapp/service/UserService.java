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

    /**
     * Repository containing user data.
     */
    private final UserRepository userRepository;

    /**
     * Mapper for users.
     */
    private final UserMapper userMapper;

    /**
     * Util class to retrieve the current user.
     */
    private final SecurityUtil securityUtil;

    /**
     * Retrieves the currently authenticated user.
     * @return the currently authenticated user.
     */
    @Transactional(readOnly = true)
    public UserResponseDto getUser() {
        User user = securityUtil.getCurrentUser();

        UserResponseDto response = userMapper.toResponseDto(user);
        return response;
    }

    /**
     * Updates the currently authenticated user.
     * @param dto The to be changed values of the user.
     * @return The ID of the updated User.
     */
    @Transactional
    public UUID updateUser(final UserUpdateDto dto) {
        User entityToUpdate = securityUtil.getCurrentUser();

        dto.name().ifPresent(entityToUpdate::setName);
        dto.email().ifPresent(entityToUpdate::setEmail);

        userRepository.save(entityToUpdate);
        log.info("Updated user with id: {}", entityToUpdate.getId());

        return entityToUpdate.getId();
    }

    /**
     * Deletes the currently authenticated user.
     */
    @Transactional
    public void deleteUser() {
        User entity = securityUtil.getCurrentUser();

        userRepository.delete(entity);
        log.info("Deleted user with id: {}", entity.getId());
    }
}
