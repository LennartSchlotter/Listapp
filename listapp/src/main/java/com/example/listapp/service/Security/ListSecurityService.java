package com.example.listapp.service.security;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.example.listapp.entity.ListEntity;
import com.example.listapp.entity.User;
import com.example.listapp.repository.ListRepository;
import com.example.listapp.security.SecurityUtil;

import lombok.RequiredArgsConstructor;

@Component("listSecurity")
@RequiredArgsConstructor
public class ListSecurityService {

    /**
     * Repository containing list data.
     */
    private final ListRepository listRepository;

    /**
     * Util class to retrieve the current user.
     */
    private final SecurityUtil securityUtil;

    /**
     * Helper method to determine if the authenticated user owns a list.
     * @param listId The ID of the list to perform the check for.
     * @return if the currently authenticated user is the owner of the list.
     */
    public boolean isOwner(final UUID listId) {
        User currentUser = securityUtil.getCurrentUser();
        Optional<ListEntity> optional = listRepository.findByIdAndOwnerId(
            listId, currentUser.getId()
        );
        return optional.isPresent();
    }
}
