package com.zerowaste.models.broadcast;

import java.time.LocalDate;
import java.util.List;

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

    public BroadcastEmail(String email) {
        this.email = email;
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
    private List<BroadcastList> broadcastLists;

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

    public List<BroadcastList> getBroadcastLists() {
        return broadcastLists;
    }

    public void setBroadcastLists(List<BroadcastList> broadcastLists) {
        this.broadcastLists = broadcastLists;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((email == null) ? 0 : email.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BroadcastEmail other = (BroadcastEmail) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (email == null) {
            if (other.email != null)
                return false;
        } else if (!email.equals(other.email))
            return false;
        return true;
    }
}
