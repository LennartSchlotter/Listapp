package com.example.listapp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.listapp.dto.item.ItemCreateDto;
import com.example.listapp.dto.item.ItemResponseDto;
import com.example.listapp.dto.item.ItemSummaryDto;
import com.example.listapp.entity.Item;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    /**
     * Maps an Item entity to a response dto.
     * @param entity the entity to be mapped.
     * @return the ResponseDTO.
     */
    ItemResponseDto toResponseDto(Item entity);

    /**
     * Maps an Item entity to a summary dto.
     * @param entity the entity to be mapped.
     * @return the Summary DTO.
     */
    ItemSummaryDto toSummaryDto(Item entity);

    /**
     * Maps an ItemCreateDto to an entity.
     * @param dto the dto to be mapped.
     * @return the Entity.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "list", ignore = true)
    @Mapping(target = "position", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "version", ignore = true)
    Item toEntity(ItemCreateDto dto);
}
