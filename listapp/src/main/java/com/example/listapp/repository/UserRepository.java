package com.example.listapp.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.listapp.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    /**
     * Method to find a specific user by his name.
     * @param Name String value of the name the user chose.
     * @return a user if the name in question matches any value in the database.
     */
    Optional<User> findByName(String name);
}
