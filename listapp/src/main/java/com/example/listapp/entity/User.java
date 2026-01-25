package com.example.listapp.entity;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import com.example.listapp.helper.Constraints;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
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

/**
 * Database table definition for a User.
 */
@DynamicUpdate
@Getter
@Setter
@Entity
@Table(name = "users")
@SuppressFBWarnings("EI_EXPOSE_REP")
public class User {

    /**
     * The unique identifier of the user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false)
    private UUID id;

    /**
     * The OAuth2 provider for the user.
     */
    @Column(name = "oauth2_provider", nullable = false)
    private String oauth2Provider;

    /**
     * The unique identifier for the OAuth2 provider.
     */
    @Column(name = "oauth2_sub", nullable = false)
    private String oauth2Sub;

    /**
     * The name of the user.
     */
    @NotBlank(message = "Name is required")
    @Size(max = Constraints.NAME_MAX_LENGTH)
    @Column(length = Constraints.NAME_MAX_LENGTH,
        nullable = false, unique = true)
    private String name;

    /**
     * The email of the user.
     */
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Size(max = Constraints.EMAIL_MAX_LENGTH)
    @Column(length = Constraints.EMAIL_MAX_LENGTH,
        nullable = false, unique = true)
    private String email;

    /**
     * The lists associated with the user.
     */
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL,
        orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<ListEntity> lists = new HashSet<>();

    /**
     * Timestamp of the creation of the user.
     */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    /**
     * Timestamp of the last update of the user.
     */
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    /**
     * Version of the user.
     */
    @Version
    private Long version;
}
