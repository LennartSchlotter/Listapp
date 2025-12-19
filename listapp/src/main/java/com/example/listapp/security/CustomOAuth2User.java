package com.example.listapp.security;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.example.listapp.entity.User;

/**
 * Custom implementation of OAuth2User that associates the OAuth2 authentication data with the application's User Entity.
 */
public class CustomOAuth2User implements OAuth2User {
    private final OAuth2User _oauth2User;
    private final User _user;

    public CustomOAuth2User(OAuth2User oauth2User, User user){
        _oauth2User = oauth2User;
        _user = user;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return _oauth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();  // no roles yet
    }

    @Override
    public String getName() {
        return _oauth2User.getAttribute("sub");
    }

    /**
     * Returns the associated application User entity.
     * @return the User instance.
     */
    public User getUser() {
        return _user;
    }
}
