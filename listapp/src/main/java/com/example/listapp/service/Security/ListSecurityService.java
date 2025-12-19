package com.example.listapp.service.Security;

import java.util.UUID;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.example.listapp.entity.User;
import com.example.listapp.repository.ListRepository;
import com.example.listapp.security.CustomOAuth2User;

@Component("listSecurity")
public class ListSecurityService {
    private final ListRepository _listRepository;

    public ListSecurityService(ListRepository listRepository){
        _listRepository = listRepository;
    }

    /**
     * Helper method to determine whether the currently authenticated user owns a specified list.
     * @param listId The ID of the list to perform the check for.
     * @return a boolean representing whether or not the currently authenticated user is the owner of the requested list.
     */
    public boolean isOwner(UUID listId){
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
