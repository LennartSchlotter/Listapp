package com.example.listapp.repository;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import com.example.listapp.entity.User;

import static com.example.listapp.util.builder.UserTestBuilder.aUser;

@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void findByName_shouldReturnUser_whenNameMatches() {
        User user = aUser().withName("Thomas").withEmail("thomas@example.com").build();
        entityManager.persistAndFlush(user);
        
        var result = userRepository.findByName("Thomas");

        assertTrue(result.isPresent());
        assertTrue(result.get().getName().equals("Thomas"));
        assertTrue(result.get().getEmail().equals("thomas@example.com"));
    }

    @Test
    void findByName_shouldReturnEmpty_whenNoMatch() {
        User user = aUser().withName("Thomas").build();
        entityManager.persistAndFlush(user);

        var result = userRepository.findByName("Anna");

        assertTrue(result.isEmpty());
    }

    @Test
    void findByEmail_shouldReturnUser_whenEmailMatches() {
        User user = aUser().withName("Thomas").withEmail("thomas@test.com").build();
        entityManager.persistAndFlush(user);
        
        var result = userRepository.findByEmail("thomas@test.com");

        assertTrue(result.isPresent());
        assertTrue(result.get().getName().equals("Thomas"));
        assertTrue(result.get().getEmail().equals("thomas@test.com"));
    }

    @Test
    void findByEmail_shouldReturnEmpty_whenNoMatch() {
        User user = aUser().withEmail("sophie@mail.de").build();
        entityManager.persistAndFlush(user);

        var result = userRepository.findByEmail("Sophie");

        assertTrue(result.isEmpty());
    }

    @Test
    void findByOauth2ProviderAndOAuth2Sub_shouldReturnUser_whenBothMatch() {
        User user = aUser().build();
        entityManager.persistAndFlush(user);

        var result = userRepository.findByOauth2ProviderAndOauth2Sub("google", "oauth-sub-123");

        assertTrue(result.isPresent());
        assertTrue(result.get().getOauth2Provider().equals("google"));
        assertTrue(result.get().getOauth2Sub().equals("oauth-sub-123"));
    }

    @Test
    void findByOauth2ProviderAndOAuth2Sub_shouldReturnEmpty_whenProviderDiffers() {
        User user = aUser().build();
        entityManager.persistAndFlush(user);

        var result = userRepository.findByOauth2ProviderAndOauth2Sub("github", "oauth-sub-123");

        assertTrue(result.isEmpty());
    }

    @Test
    void findByOauth2ProviderAndOAuth2Sub_shouldReturnEmpty_whenSubDiffers() {
        User user = aUser().build();
        entityManager.persistAndFlush(user);

        var result = userRepository.findByOauth2ProviderAndOauth2Sub("google", "oauth-sub-124");

        assertTrue(result.isEmpty());
    }
}
