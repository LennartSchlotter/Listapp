package com.example.listapp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.listapp.dto.user.UserCreateDto;
import com.example.listapp.dto.user.UserResponseDto;
import com.example.listapp.dto.user.UserSummaryDto;
import com.example.listapp.entity.User;

@Mapper(componentModel = "spring", uses = ListMapper.class)
public interface UserMapper {

    @Mapping(target = "lists", source= "lists")
    UserResponseDto toResponseDto(User entity);
    
    UserSummaryDto toSummaryDto(User entity);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "lists", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "version", ignore = true)
    User toEntity(UserCreateDto dto);
}
