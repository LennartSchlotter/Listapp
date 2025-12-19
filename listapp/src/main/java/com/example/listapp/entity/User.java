package com.example.listapp.entity;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@DynamicUpdate
@Getter
@Setter
@Entity
@Table(name="users")
/**
 * Database table definition for a User.
 */
public class User {

    @Id 
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid", nullable = false)
    private UUID id;
    
    @Column(name = "oauth2_provider", nullable = false)
    private String oauth2Provider;

    @Column(name = "oauth2_sub", nullable = false)
    private String oauth2Sub;

    @NotBlank(message = "Name is required")
    @Size(max = 64)
    @Column(length = 64, nullable = false, unique = true)
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Size(max = 255)
    @Column(length = 255, nullable = false, unique = true)
    private String email;
    
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<ListEntity> lists = new HashSet<>();
    
    @CreationTimestamp
    @Column(name="created_at", nullable = false, updatable = false)
    private Instant createdAt;
    
    @UpdateTimestamp
    @Column(name="updated_at", nullable = false)
    private Instant updatedAt;

    @Version
    private Long version;
}
