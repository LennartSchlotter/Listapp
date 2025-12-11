package com.example.listapp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.listapp.dto.item.ItemCreateDto;
import com.example.listapp.dto.item.ItemResponseDto;
import com.example.listapp.dto.item.ItemSummaryDto;
import com.example.listapp.entity.Item;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    ItemResponseDto toResponseDto(Item entity);
    
    ItemSummaryDto toSummaryDto(Item entity);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "listId", ignore = true)
    @Mapping(target = "list", ignore = true)
    @Mapping(target = "position", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "version", ignore = true)
    Item toEntity(ItemCreateDto dto);
}
