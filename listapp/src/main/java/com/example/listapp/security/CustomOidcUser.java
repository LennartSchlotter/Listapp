package com.example.listapp.security;

import java.util.Collection;

import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;

import com.example.listapp.entity.User;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public class CustomOidcUser extends DefaultOidcUser {

    /**
     * The User Entity to create a CustomOidcUser for.
     */
    private final transient User user;

    /**
     * Version number for the serialized form of the class.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor for the Custom User.
     * @param authorities The Spring Securiyt authorities assigned to the user.
     * @param idToken Token issued by OpenID provider during auth.
     * @param userInfo Response from the UserInfo endpoint.
     * @param userField The custom user.
     */
    @SuppressFBWarnings(
        value = "EI_EXPOSE_REP2",
        justification = "JPA entity stored intentionally in principal"
    )
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
    @SuppressFBWarnings(
        value = "EI_EXPOSE_REP",
        justification = "Security principal intentionally exposes Jpa entity"
    )
    public User getUser() {
        return user;
    }

    /**
     * Overrides the equals function to enable entity-equality checks.
     */
    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof CustomOidcUser other)) {
            return false;
        }
        return user.getId().equals(other.user.getId());
    }

    /**
     * Overrides the hashCode function to enable retrieving a user's hash code.
     */
    @Override
    public int hashCode() {
        return user.getId().hashCode();
    }
}
