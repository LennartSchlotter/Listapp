package com.example.listapp.service.Security;

import java.util.UUID;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.example.listapp.entity.User;
import com.example.listapp.repository.ItemRepository;
import com.example.listapp.repository.ListRepository;
import com.example.listapp.security.CustomOAuth2User;

@Component("itemSecurity")
public class ItemSecurityService {
    private final ItemRepository _itemRepository;
    private final ListRepository _listRepository;

    public ItemSecurityService(ItemRepository itemRepository, ListRepository listRepository){
        _itemRepository = itemRepository;
        _listRepository = listRepository;
    }

    /**
     * Helper method to determine whether a user can access a specific item.
     * @param listId The ID of the list the item to be retrieved belongs to.
     * @param itemId The ID of the item to perform the check for.
     * @return a boolean representing whether or not the currently authenticated user can access the requested item.
     */
    public boolean canAccessItem(UUID listId, UUID itemId) {
        User currentUser = getCurrentUser();
        return (_itemRepository.findByIdAndListId(itemId, listId) != null) 
            && (_listRepository.findByIdAndOwnerId(listId, currentUser.getId()) != null);
    }

    /**
     * Helper method to determine whether a user can access a specific list.
     * @param listId The ID of the list to perform the check for.
     * @return a boolean representing whether or not the currently authenticated user can access the requested list.
     */
    public boolean canAccessList(UUID listId) {
        User currentUser = getCurrentUser();
        return (_listRepository.findByIdAndOwnerId(listId, currentUser.getId()) != null);
    }

    private User getCurrentUser() {
        CustomOAuth2User principal = (CustomOAuth2User) SecurityContextHolder
            .getContext()
            .getAuthentication()
            .getPrincipal();
        return principal.getUser();
    }
}
