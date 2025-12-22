package com.example.listapp.service.Security;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.example.listapp.entity.User;
import com.example.listapp.repository.ListRepository;
import com.example.listapp.security.SecurityUtil;

@Component("listSecurity")
public class ListSecurityService {

    private final ListRepository _listRepository;
    private final SecurityUtil _securityUtil;

    public ListSecurityService(ListRepository listRepository, SecurityUtil securityUtil){
        _listRepository = listRepository;
        _securityUtil = securityUtil;
    }

    /**
     * Helper method to determine whether the currently authenticated user owns a specified list.
     * @param listId The ID of the list to perform the check for.
     * @return a boolean representing whether or not the currently authenticated user is the owner of the requested list.
     */
    public boolean isOwner(UUID listId){
        User currentUser = _securityUtil.getCurrentUser();
        return (_listRepository.findByIdAndOwnerId(listId, currentUser.getId()) != null);
    }
}
