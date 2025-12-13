package com.example.listapp.repository;

import java.util.List;
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
     * Additional check to ensure a user can only access their own lists.
     * @param listId The ID of the list to perform the check for.
     * @param ownerId The ID of the user to perform the check for.
     * @return a boolean value, representing whether or not the ID in question belongs to the user.
     */
    boolean existsByIdAndOwnerId(UUID listId, UUID ownerId);
}
