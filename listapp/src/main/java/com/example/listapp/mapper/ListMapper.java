package com.example.listapp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.listapp.dto.list.ListCreateDto;
import com.example.listapp.dto.list.ListResponseDto;
import com.example.listapp.dto.list.ListSummaryDto;
import com.example.listapp.entity.ListEntity;

@Mapper(componentModel = "spring", uses = {ItemMapper.class})
public interface ListMapper {

    @Mapping(target = "owner", source = "owner")
    @Mapping(target = "items", source = "items")
    ListResponseDto toResponseDto(ListEntity entity);
    
    @Mapping(target = "itemCount", expression = "java(entity.getItems() != null ? entity.getItems().size() : 0)")
    ListSummaryDto toSummaryDto(ListEntity entity);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "items", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "version", ignore = true)
    ListEntity toEntity(ListCreateDto dto);
}
