package com.example.listapp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.listapp.dto.user.UserCreateDto;
import com.example.listapp.dto.user.UserResponseDto;
import com.example.listapp.dto.user.UserSummaryDto;
import com.example.listapp.entity.User;

@Mapper(componentModel = "spring", uses = ListMapper.class)
public interface UserMapper {

    /**
     * Maps a user entity to a response dto.
     * @param entity the entity to be mapped.
     * @return the response dto.
     */
    @Mapping(target = "lists", source = "lists")
    UserResponseDto toResponseDto(User entity);

    /**
     * Maps a user entity to a summary dto.
     * @param entity the entity to be mapped.
     * @return the summary dto.
     */
    UserSummaryDto toSummaryDto(User entity);

    /**
     * Maps a UserCreateDto to an entity.
     * @param dto the dto to be mapped.
     * @return the User entity.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "lists", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    User toEntity(UserCreateDto dto);
}
