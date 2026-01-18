package com.example.listapp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.listapp.dto.list.ListCreateDto;
import com.example.listapp.dto.list.ListResponseDto;
import com.example.listapp.dto.list.ListSummaryDto;
import com.example.listapp.entity.ListEntity;

@Mapper(componentModel = "spring", uses = {ItemMapper.class})
public interface ListMapper {

    /**
     * Maps a List entity to a response dto.
     * @param entity the entity to be mapped.
     * @return the response dto.
     */
    @Mapping(target = "owner", source = "owner")
    @Mapping(target = "items", source = "items")
    ListResponseDto toResponseDto(ListEntity entity);

    /**
     * Maps a List entity to a summary dto.
     * @param entity the entity to be mapped.
     * @return the response dto.
     */
    @Mapping(target = "itemCount", expression =
        "java(entity.getItems() != null ? entity.getItems().size() : 0)")
    ListSummaryDto toSummaryDto(ListEntity entity);

    /**
     * Maps a ListCreateDto to an entity.
     * @param dto the dto to be mapped.
     * @return the list entity.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "items", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "version", ignore = true)
    ListEntity toEntity(ListCreateDto dto);
}
