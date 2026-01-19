package com.example.listapp.security;

import java.util.Collection;

import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;

import com.example.listapp.entity.User;

public class CustomOidcUser extends DefaultOidcUser {

    /**
     * The User Entity to create a CustomOidcUser for.
     */
    private final User user;

    /**
     * Constructor for the Custom User.
     * @param authorities The Spring Securiyt authorities assigned to the user.
     * @param idToken Token issued by OpenID provider during auth.
     * @param userInfo Response from the UserInfo endpoint.
     * @param userField The custom user.
     */
    public CustomOidcUser(
        final Collection authorities,
        final OidcIdToken idToken,
        final OidcUserInfo userInfo,
        final User userField
    ) {
        super(authorities, idToken, userInfo);
        user = userField;
    }

    /**
     * Getter for the User object.
     * @return the user.
     */
    public User getUser() {
        return user;
    }
}
