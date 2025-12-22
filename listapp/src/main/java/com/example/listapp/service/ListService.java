package com.example.listapp.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.listapp.dto.list.ListCreateDto;
import com.example.listapp.dto.list.ListResponseDto;
import com.example.listapp.dto.list.ListUpdateDto;
import com.example.listapp.entity.ListEntity;
import com.example.listapp.entity.User;
import com.example.listapp.exception.custom.AccessDeniedException;
import com.example.listapp.exception.custom.ResourceNotFoundException;
import com.example.listapp.mapper.ListMapper;
import com.example.listapp.repository.ListRepository;
import com.example.listapp.security.SecurityUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ListService {

    private final ListRepository _listRepository;
    private final ListMapper _listMapper;
    private final SecurityUtil _securityUtil;
    
    /**
     * Retrieves all lists associated with the authenticated user.
     * @return a list of lists.
     */
    @Transactional(readOnly = true)
    public List<ListResponseDto> getAllUserLists() {
        UUID ownerId = _securityUtil.getCurrentUser().getId();
        List<ListEntity> entityList = _listRepository.findAllByOwnerId(ownerId);

        List<ListResponseDto> dtoList = entityList.stream()
            .map(entity -> _listMapper.toResponseDto(entity))
            .collect(Collectors.toList());

        return dtoList;
    }

    /**
     * Retrieves a specific list.
     * @param id The ID of the list to be retrieved.
     * @return The List with the specified ID.
     */
    @Transactional(readOnly = true)
    public ListResponseDto getListById(UUID id) {
        ListEntity entity = _listRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("List", id.toString()));

        validateOwnership(entity);

        ListResponseDto response = _listMapper.toResponseDto(entity);
        
        return response;
    }

    /**
     * Creates a list.
     * @param dto The list to be created.
     * @return The ID of the created list.
     */
    @Transactional
    public UUID createList(ListCreateDto dto) {
        ListEntity entity = _listMapper.toEntity(dto);

        User currentUser = _securityUtil.getCurrentUser();
        entity.setOwner(currentUser);

        ListEntity savedEntity =_listRepository.save(entity);
        log.info("Created list with id: {} for user: {}", savedEntity.getId(), currentUser.getId());

        return savedEntity.getId();
    }

    /**
     * Updates a list.
     * @param id The ID of the list to update.
     * @param dto The to be changed values of the list.
     * @return The ID of the updated list.
     */
    @Transactional
    public UUID updateList(UUID id, ListUpdateDto dto) {
        ListEntity entityToUpdate = _listRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("List", id.toString()));
        
        validateOwnership(entityToUpdate);

        dto.title().ifPresent(entityToUpdate::setTitle);
        dto.description().ifPresent(entityToUpdate::setDescription);

        _listRepository.save(entityToUpdate);
        log.info("Updated list with id: {}", id);

        return entityToUpdate.getId();
    }

    /**
     * Deletes a list.
     * @param id The ID of the list to delete.
     */
    @Transactional
    public void deleteList(UUID id) {
        ListEntity entity = _listRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("List", id.toString()));
        
        validateOwnership(entity);

        entity.markAsDeleted();
        _listRepository.save(entity);
        log.info("Soft deleted list with id: {}", id);
    }

    private void validateOwnership(ListEntity entity) {
        User currentUser = _securityUtil.getCurrentUser();
        if (!entity.getOwner().getId().equals(currentUser.getId())) {
            log.warn("User {} attempted to access list {} owned by user {}", 
                currentUser.getId(), entity.getId(), entity.getOwner().getId());
            throw new AccessDeniedException("You don't have permission to access this list");
        }
    }
}
