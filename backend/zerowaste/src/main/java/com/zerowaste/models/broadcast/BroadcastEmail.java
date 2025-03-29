package com.zerowaste.models.broadcast;

import java.time.LocalDate;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Table(name = "broadcast_emails")
@Entity(name = "broadcast_emails")
public class BroadcastEmail {
    public BroadcastEmail() {}

    public BroadcastEmail(
        Long id,
        String email,
        LocalDate createdAt,
        LocalDate updatedAt,
        LocalDate deletedAt,
        Set<BroadcastList> broadcastLists
    ) {
        this.id = id;
        this.email = email;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
        this.broadcastLists = broadcastLists;
    }

    @Id
    @SequenceGenerator(name = "broadcast_emails_id_seq", sequenceName = "broadcast_emails_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "broadcast_emails_id_seq")
    private Long id;

    @Column(name = "email", nullable = false, length = 320)
    private String email;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "updated_at", nullable = true)
    private LocalDate updatedAt;

    @Column(name = "deleted_at", nullable = true)
    private LocalDate deletedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDate.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDate.now();
    }

    @ManyToMany
    @JoinTable(name = "broadcast_emails_broadcast_lists", joinColumns = @JoinColumn(name = "broadcast_emails_id"), inverseJoinColumns = @JoinColumn(name = "broadcast_lists_id"))
    private Set<BroadcastList> broadcastLists;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDate getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDate deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Set<BroadcastList> getBroadcastLists() {
        return broadcastLists;
    }

    public void setBroadcastLists(Set<BroadcastList> broadcastLists) {
        this.broadcastLists = broadcastLists;
    }
}
