package com.example.listapp.service.Security;

import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import com.example.listapp.entity.User;
import com.example.listapp.repository.UserRepository;
import com.example.listapp.security.CustomOidcUser;

@Service
public class AuthenticationService extends OidcUserService {
    
    private final UserRepository _userRepository;

    public AuthenticationService(UserRepository userRepository){
        _userRepository = userRepository;
    }

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId();
        String sub = (String) oidcUser.getSubject();
        String email = (String) oidcUser.getEmail();
        String name = (String) oidcUser.getFullName();

        // Create or update the user based on the OAuth data.
        User userEntity = _userRepository.findByOauth2ProviderAndOauth2Sub(provider, sub)
            .map(existingUser -> {
                existingUser.setName(name);
                existingUser.setEmail(email);
                return _userRepository.save(existingUser);
            })
            .orElseGet(() -> {
                User newUser = new User();
                newUser.setOauth2Provider(provider);
                newUser.setOauth2Sub(sub);
                newUser.setEmail(email);
                newUser.setName(name);
                return _userRepository.save(newUser);
            });
        
        return new CustomOidcUser(oidcUser.getAuthorities(), oidcUser.getIdToken(), oidcUser.getUserInfo(), userEntity);
    }
}
