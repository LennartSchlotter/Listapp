package com.example.listapp.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.listapp.dto.list.ListCreateDto;
import com.example.listapp.dto.list.ListResponseDto;
import com.example.listapp.dto.list.ListUpdateDto;
import com.example.listapp.entity.ListEntity;
import com.example.listapp.mapper.ListMapper;
import com.example.listapp.repository.ListRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ListService {

    private final ListRepository _listRepository;
    private final ListMapper _listMapper;
    
    public List<ListResponseDto> getAllUserLists() {
        UUID ownerId = null;
        //TODO get ownerId from auth
        List<ListEntity> entityList = _listRepository.findAllByOwnerId(ownerId);

        List<ListResponseDto> dtoList = entityList.stream()
            .map(entity -> _listMapper.toResponseDto(entity))
            .collect(Collectors.toList());

        return dtoList;
    }

    public ListResponseDto getListById(UUID id) {
        UUID ownerId = null;
        //TODO get ownerId from auth
        ListEntity entity = _listRepository.findByIdAndOwnerId(id, ownerId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "List not found with id: " + id));
        ListResponseDto response = _listMapper.toResponseDto(entity);
        
        return response;
    }

    public UUID createList(ListCreateDto dto) {
        ListEntity entity = _listMapper.toEntity(dto);
        ListEntity savedEntity =_listRepository.save(entity);
        return savedEntity.getId();
    }

    public UUID updateList(UUID id, ListUpdateDto dto) {
        UUID ownerId = null;
        //TODO get ownerId from auth
        ListEntity entityToUpdate = _listRepository.findByIdAndOwnerId(id, ownerId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "List not found with id: " + id));
        
        dto.title().ifPresent(entityToUpdate::setTitle);
        dto.description().ifPresent(entityToUpdate::setDescription);

        _listRepository.save(entityToUpdate);
        return entityToUpdate.getId();
    }

    public void deleteList(UUID id) {
        UUID ownerId = null;
        //TODO get ownerId from auth
        ListEntity entity = _listRepository.findByIdAndOwnerId(id, ownerId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "List not found with id: " + id));
        
        entity.markAsDeleted();
        _listRepository.save(entity);
    }
}
