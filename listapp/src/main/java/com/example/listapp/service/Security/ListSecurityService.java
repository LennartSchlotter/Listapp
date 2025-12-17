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
