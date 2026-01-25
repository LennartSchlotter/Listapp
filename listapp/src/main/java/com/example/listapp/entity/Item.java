package com.example.listapp.entity;

import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UpdateTimestamp;

import com.example.listapp.helper.Constraints;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * Database table definition for an Item.
 */
@DynamicUpdate
@Getter
@Setter
@SQLRestriction("deleted = false")
@Entity
@Table(name = "list_items", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"list_id", "position"})
})
@SuppressFBWarnings("EI_EXPOSE_REP")
public class Item {

    /**
     * Unique identifier of the Item.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid", nullable = false)
    private UUID id;

    /**
     * Entity of the list the item belongs to.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "list_id", nullable = false)
    private ListEntity list;

    /**
     * Position of the item within the list.
     */
    @Column(nullable = false)
    private Integer position;

    /**
     * Title of the item.
     */
    @NotBlank(message = "Title is required")
    @Size(max = Constraints.TITLE_MAX_LENGTH)
    @Column(length = Constraints.TITLE_MAX_LENGTH, nullable = false)
    private String title;

    /**
     * Optional field to display notes for the item.
     */
    @Column(name = "metadata", columnDefinition = "text", nullable = true)
    private String notes;

    /**
     * Optional URL of an image associated with the item.
     */
    @Size(max = Constraints.IMAGE_PATH_MAX_LENGTH)
    @Column(name = "image_path", length =
        Constraints.IMAGE_PATH_MAX_LENGTH, nullable = true)
    private String imagePath;

    /**
     * Timestamp of the creation of the item.
     */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    /**
     * Timestamp of the last update of the item.
     */
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    /**
     * Soft-delete flag for the item.
     */
    @Column(nullable = false)
    private boolean deleted = false;

    /**
     * Version of the Item.
     */
    @Version
    private Long version;

    /**
     * Helper method to determine the deletion state of an item.
     * @return a boolean of the deleted value.
     */
    public boolean isDeleted() {
        return deleted;
    }

    /**
     * Helper method to mark an item as deleted.
     */
    public void markAsDeleted() {
        this.deleted = true;
    }
}
