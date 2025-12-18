package com.example.listapp.entity;

import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UpdateTimestamp;

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

@DynamicUpdate
@Getter
@Setter
@SQLRestriction("deleted = false")
@Entity
@Table(name="list_items", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"list_id", "position"})
})
public class Item {
    @Id 
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid", nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "list_id", nullable = false)
    private ListEntity list;

    @Column(nullable = false)
    private Integer position;

    @NotBlank(message = "Title is required")
    @Size(max = 100)
    @Column(length = 100, nullable = false)
    private String title;

    @Column(name = "metadata", columnDefinition = "text", nullable = true)
    private String notes;

    @Size(max = 1024)
    @Column(name = "image_path", length = 1024, nullable = true)
    private String imagePath;

    @CreationTimestamp
    @Column(name="created_at", nullable = false, updatable = false)
    private Instant createdAt;
    
    @UpdateTimestamp
    @Column(name="updated_at", nullable = false)
    private Instant updatedAt;

    @Column(nullable = false)
    private boolean deleted = false;

    @Version
    private Long version;

    /**
     * Helper method to determine the deletion state of an item.
     * @return a boolean of the deleted value.
     */
    public boolean isDeleted(){
        return deleted;
    }

    /**
     * Helper method to mark an item as deleted.
     */
    public void markAsDeleted() {
        this.deleted = true;
    }
}
