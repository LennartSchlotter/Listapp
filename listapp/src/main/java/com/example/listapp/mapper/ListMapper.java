package com.example.listapp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.listapp.dto.list.ListCreateDto;
import com.example.listapp.dto.list.ListResponseDto;
import com.example.listapp.dto.list.ListSummaryDto;
import com.example.listapp.entity.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class, ItemMapper.class})
public interface ListMapper {

    @Mapping(target = "owner", source = "owner")
    @Mapping(target = "items", source = "items")
    ListResponseDto toResponseDto(List entity);
    
    @Mapping(target = "itemCount", expression = "java(entity.getItems() != null ? entity.getItems().size() : 0)")
    ListSummaryDto toSummaryDto(List entity);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ownerId", ignore = true)
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "items", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "version", ignore = true)
    List toEntity(ListCreateDto dto);
}
