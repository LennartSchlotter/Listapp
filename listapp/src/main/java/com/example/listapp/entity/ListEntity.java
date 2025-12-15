package com.example.listapp.entity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UpdateTimestamp;

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

@DynamicUpdate
@Getter
@Setter
@SQLRestriction("deleted = false")
@Entity
@Table(name="lists")
public class ListEntity {

    @Id 
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid", nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id", nullable = false, updatable = false)
    private User owner;

    @NotBlank(message = "Title is required")
    @Size(max = 100)
    @Column(length = 100, nullable=false)
    private String title;
    
    @Size(max = 2000)
    @Column(columnDefinition = "text", nullable = true)
    private String description;
    
    @CreationTimestamp
    @Column(name="created_at", nullable = false, updatable = false)
    private Instant createdAt;
    
    @UpdateTimestamp
    @Column(name="updated_at", nullable = false)
    private Instant updatedAt;
    
    @OneToMany(mappedBy = "list", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("position ASC")
    private List<Item> items = new ArrayList<>();

    @Column(nullable = false)
    private boolean deleted = false;

    @Version
    private Long version;

    /**
     * Helper method to determine the deletion state of a list.
     * @return a boolean of the deleted value.
     */
    public boolean isDeleted(){
        return deleted;
    }

    /**
     * Helper method to mark a list as deleted.
     */
    public void markAsDeleted() {
        this.deleted = true;
    }
}
