package com.example.listapp.service.Security;

import java.util.Map;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.example.listapp.entity.User;
import com.example.listapp.repository.UserRepository;
import com.example.listapp.security.CustomOAuth2User;

@Service
public class AuthenticationService extends DefaultOAuth2UserService {
    
    private final UserRepository _userRepository;

    public AuthenticationService(UserRepository userRepository){
        _userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);

        Map<String, Object> attributes = oauth2User.getAttributes();
        String provider = userRequest.getClientRegistration().getRegistrationId();
        String sub = (String) attributes.get("sub");
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");

        User user = _userRepository.findByOauth2ProviderAndOauth2Sub(provider, sub)
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
        
        return new CustomOAuth2User(oauth2User, user);
    }
}
