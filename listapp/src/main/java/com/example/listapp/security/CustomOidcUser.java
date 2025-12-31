package com.example.listapp.security;

import java.util.Collection;

import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;

import com.example.listapp.entity.User;

public class CustomOidcUser extends DefaultOidcUser {
    private final User _user;

    public CustomOidcUser(Collection authorities, OidcIdToken idToken, OidcUserInfo userInfo, User user) {
        super(authorities, idToken, userInfo);
        _user = user;
    }

    public User getUser() {
        return _user;
    }
}
