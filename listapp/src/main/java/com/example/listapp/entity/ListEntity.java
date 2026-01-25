package com.example.listapp.entity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLRestriction;
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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * Database table definition for a List.
 */
@DynamicUpdate
@Getter
@Setter
@SQLRestriction("deleted = false")
@Entity
@Table(name = "lists")
@SuppressFBWarnings("EI_EXPOSE_REP")
public class ListEntity {

    /**
     * The unique identifier of the list.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid", nullable = false)
    private UUID id;

    /**
     * The Entity of the owner of the list.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id", nullable = false, updatable = false)
    private User owner;

    /**
     * The Title of the list.
     */
    @NotBlank(message = "Title is required")
    @Size(max = Constraints.TITLE_MAX_LENGTH)
    @Column(length = Constraints.TITLE_MAX_LENGTH, nullable = false)
    private String title;

    /**
     * The description of the list.
     */
    @Size(max = Constraints.DESCRIPTION_MAX_LENGTH)
    @Column(columnDefinition = "text", nullable = true)
    private String description;

    /**
     * Timestamp of the creation of the list.
     */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    /**
     * Timestamp of the last update of the list.
     */
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    /**
     * A list of the items associated with the list.
     */
    @OneToMany(mappedBy = "list", cascade = CascadeType.ALL,
        orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("position ASC")
    private List<Item> items = new ArrayList<>();

    /**
     * Soft-delete flag for the list.
     */
    @Column(nullable = false)
    private boolean deleted = false;

    /**
     * Version of the list.
     */
    @Version
    private Long version;

    /**
     * Helper method to determine the deletion state of a list.
     * @return a boolean of the deleted value.
     */
    public boolean isDeleted() {
        return deleted;
    }

    /**
     * Helper method to mark a list as deleted.
     */
    public void markAsDeleted() {
        this.deleted = true;
    }
}
