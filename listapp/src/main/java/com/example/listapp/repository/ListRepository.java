package com.example.listapp.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.listapp.entity.ListEntity;

public interface ListRepository extends JpaRepository<ListEntity, UUID> {

    /**
     * Method to return all lists belonging to a specific user.
     * @param ownerId ID of the user to fetch lists for.
     * @return a list of lists.
     */
    List<ListEntity> findAllByOwnerId(UUID ownerId);

    /**
     * Method to return a specific list belonging to a specific user.
     * @param listId The ID of the list to perform the check for.
     * @param ownerId The ID of the user to perform the check for.
     * @return a list with specified id, belonging to the specified user.
     */
    Optional<ListEntity> findByIdAndOwnerId(UUID listId, UUID ownerId);
}
