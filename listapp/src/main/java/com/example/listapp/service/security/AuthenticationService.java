package com.example.listapp.service.security;

import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import com.example.listapp.entity.User;
import com.example.listapp.repository.UserRepository;
import com.example.listapp.security.CustomOidcUser;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public final class AuthenticationService extends OidcUserService {

    /**
     * Repository containing user data.
     */
    private final UserRepository userRepository;

    @Override
    public OidcUser loadUser(final OidcUserRequest userRequest)
        throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);

        String provider =
            userRequest.getClientRegistration().getRegistrationId();
        String sub = (String) oidcUser.getSubject();
        String email = (String) oidcUser.getEmail();
        String name = (String) oidcUser.getFullName();

        // Create or update the user based on the OAuth data.
        User userEntity =
            userRepository.findByOauth2ProviderAndOauth2Sub(provider, sub)
                .map(existingUser -> {
                    existingUser.setName(name);
                    existingUser.setEmail(email);
                    return userRepository.save(existingUser);
                })
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setOauth2Provider(provider);
                    newUser.setOauth2Sub(sub);
                    newUser.setEmail(email);
                    newUser.setName(name);
                    return userRepository.save(newUser);
                });

        return new CustomOidcUser(
            oidcUser.getAuthorities(),
            oidcUser.getIdToken(),
            oidcUser.getUserInfo(),
            userEntity
        );
    }
}
