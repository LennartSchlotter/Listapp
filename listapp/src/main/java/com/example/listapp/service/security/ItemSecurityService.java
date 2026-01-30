package com.example.listapp.service.security;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.example.listapp.entity.User;
import com.example.listapp.repository.ItemRepository;
import com.example.listapp.repository.ListRepository;
import com.example.listapp.security.SecurityUtil;

import lombok.RequiredArgsConstructor;

@Component("itemSecurity")
@RequiredArgsConstructor
public class ItemSecurityService {

    /**
     * Repository containing item data.
     */
    private final ItemRepository itemRepository;

    /**
     * Repository containing list data.
     */
    private final ListRepository listRepository;

    /**
     * Util class to retrieve the current user.
     */
    private final SecurityUtil securityUtil;

    /**
     * Helper method to determine whether a user can access a specific item.
     * @param listId The ID of the list the item to be retrieved belongs to.
     * @param itemId The ID of the item to perform the check for.
     * @return if the currently authenticated user can access the item.
     */
    public boolean canAccessItem(final UUID listId, final UUID itemId) {
        User currentUser = securityUtil.getCurrentUser();
        return (itemRepository.findByIdAndListId(itemId, listId) != null)
            && (listRepository.findByIdAndOwnerId(
                listId, currentUser.getId()) != null
            );
    }

    /**
     * Helper method to determine whether a user can access a specific list.
     * @param listId The ID of the list to perform the check for.
     * @return if the currently authenticated user can access the list.
     */
    public boolean canAccessList(final UUID listId) {
        User currentUser = securityUtil.getCurrentUser();
        Optional<?> optionalList = listRepository.findByIdAndOwnerId(
            listId, currentUser.getId()
        );
        return optionalList.isPresent();
    }
}
