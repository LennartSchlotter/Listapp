package com.example.listapp.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.listapp.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    /**
     * Method to find a specific user by their name.
     * @param name String value of the name the user chose.
     * @return a user if the name in question matches any value in the database.
     */
    Optional<User> findByName(String name);

    /**
     * Method to find a specific user by their email.
     * @param email String value of the email the user chose.
     * @return a user if the email matches any value in the database.
     */
    Optional<User> findByEmail(String email);

    /**
     * Method to find a specific user by their oauth2 login.
     * @param provider the provider for the oauth2 login.
     * @param sub the unique identifier for this user.
     * @return a user if the provider details match any user in the database.
     */
    Optional<User> findByOauth2ProviderAndOauth2Sub(
        String provider,
        String sub
    );
}
