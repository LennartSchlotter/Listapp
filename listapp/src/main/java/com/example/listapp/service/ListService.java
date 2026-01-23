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

    /**
     * Repository containing list data.
     */
    private final ListRepository listRepository;

    /**
     * Mapper for lists.
     */
    private final ListMapper listMapper;

    /**
     * Util class to retrieve the current user.
     */
    private final SecurityUtil securityUtil;

    /**
     * Retrieves all lists associated with the authenticated user.
     * @return a list of lists.
     */
    @Transactional(readOnly = true)
    public List<ListResponseDto> getAllUserLists() {
        UUID ownerId = securityUtil.getCurrentUser().getId();
        List<ListEntity> entityList = listRepository.findAllByOwnerId(ownerId);

        List<ListResponseDto> dtoList = entityList.stream()
            .map(entity -> listMapper.toResponseDto(entity))
            .collect(Collectors.toList());

        return dtoList;
    }

    /**
     * Retrieves a specific list.
     * @param id The ID of the list to be retrieved.
     * @return The List with the specified ID.
     */
    @Transactional(readOnly = true)
    public ListResponseDto getListById(final UUID id) {
        ListEntity entity = listRepository.findById(id)
            .orElseThrow(()
                -> new ResourceNotFoundException("List", id.toString()));

        validateOwnership(entity);

        ListResponseDto response = listMapper.toResponseDto(entity);

        return response;
    }

    /**
     * Creates a list.
     * @param dto The list to be created.
     * @return The ID of the created list.
     */
    @Transactional
    public UUID createList(final ListCreateDto dto) {
        ListEntity entity = listMapper.toEntity(dto);

        User currentUser = securityUtil.getCurrentUser();
        entity.setOwner(currentUser);

        ListEntity savedEntity = listRepository.save(entity);
        log.info(
            "Created list with id: {} for user: {}",
            savedEntity.getId(),
            currentUser.getId()
        );

        return savedEntity.getId();
    }

    /**
     * Updates a list.
     * @param id The ID of the list to update.
     * @param dto The to be changed values of the list.
     * @return The ID of the updated list.
     */
    @Transactional
    public UUID updateList(final UUID id, final ListUpdateDto dto) {
        ListEntity entityToUpdate = listRepository.findById(id)
            .orElseThrow(()
                -> new ResourceNotFoundException("List", id.toString()));

        validateOwnership(entityToUpdate);

        dto.title().ifPresent(entityToUpdate::setTitle);
        dto.description().ifPresent(entityToUpdate::setDescription);

        listRepository.save(entityToUpdate);
        log.info("Updated list with id: {}", id);

        return entityToUpdate.getId();
    }

    /**
     * Deletes a list.
     * @param id The ID of the list to delete.
     */
    @Transactional
    public void deleteList(final UUID id) {
        ListEntity entity = listRepository.findById(id)
            .orElseThrow(()
                -> new ResourceNotFoundException("List", id.toString()));

        validateOwnership(entity);

        entity.markAsDeleted();
        listRepository.save(entity);
        log.info("Soft deleted list with id: {}", id);
    }

    private void validateOwnership(final ListEntity entity) {
        User currentUser = securityUtil.getCurrentUser();
        if (!entity.getOwner().getId().equals(currentUser.getId())) {
            log.warn("User {} attempted to access list {} owned by user {}",
                currentUser.getId(), entity.getId(), entity.getOwner().getId());
            throw new AccessDeniedException(
                "You don't have permission to access this list");
        }
    }
}
