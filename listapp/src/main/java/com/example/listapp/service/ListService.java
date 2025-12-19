package com.example.listapp.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.listapp.dto.list.ListCreateDto;
import com.example.listapp.dto.list.ListResponseDto;
import com.example.listapp.dto.list.ListUpdateDto;
import com.example.listapp.entity.ListEntity;
import com.example.listapp.entity.User;
import com.example.listapp.mapper.ListMapper;
import com.example.listapp.repository.ListRepository;
import com.example.listapp.security.CustomOAuth2User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ListService {

    private final ListRepository _listRepository;
    private final ListMapper _listMapper;
    
    @Transactional(readOnly = true)
    /**
     * Retrieves all lists associated with the authenticated user.
     * @return a list of lists.
     */
    public List<ListResponseDto> getAllUserLists() {
        UUID ownerId = getCurrentUser().getId();
        List<ListEntity> entityList = _listRepository.findAllByOwnerId(ownerId);

        List<ListResponseDto> dtoList = entityList.stream()
            .map(entity -> _listMapper.toResponseDto(entity))
            .collect(Collectors.toList());

        return dtoList;
    }

    @Transactional(readOnly = true)
    /**
     * Retrieves a specific list.
     * @param id The ID of the list to be retrieved.
     * @return The List with the specified ID.
     */
    public ListResponseDto getListById(UUID id) {
        ListEntity entity = _listRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "List not found with id: " + id));
        ListResponseDto response = _listMapper.toResponseDto(entity);
        
        return response;
    }

    @Transactional
    /**
     * Creates a list.
     * @param dto The list to be created.
     * @return The ID of the created list.
     */
    public UUID createList(ListCreateDto dto) {
        ListEntity entity = _listMapper.toEntity(dto);

        User currentUser = getCurrentUser();
        entity.setOwner(currentUser);

        ListEntity savedEntity =_listRepository.save(entity);
        return savedEntity.getId();
    }

    @Transactional
    /**
     * Updates a list.
     * @param id The ID of the list to update.
     * @param dto The to be changed values of the list.
     * @return The ID of the updated list.
     */
    public UUID updateList(UUID id, ListUpdateDto dto) {
        ListEntity entityToUpdate = _listRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "List not found with id: " + id));
        
        dto.title().ifPresent(entityToUpdate::setTitle);
        dto.description().ifPresent(entityToUpdate::setDescription);

        _listRepository.save(entityToUpdate);
        return entityToUpdate.getId();
    }

    @Transactional
    /**
     * Deletes a list.
     * @param id The ID of the list to delete.
     */
    public void deleteList(UUID id) {
        ListEntity entity = _listRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "List not found with id: " + id));
        
        entity.markAsDeleted();
        _listRepository.save(entity);
    }

    private User getCurrentUser() {
        CustomOAuth2User principal = (CustomOAuth2User) SecurityContextHolder
            .getContext()
            .getAuthentication()
            .getPrincipal();
        return principal.getUser();
    }
}
