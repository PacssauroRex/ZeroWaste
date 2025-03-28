package com.zerowaste.models.broadcast;

import java.util.Date;
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
    @Id
    @SequenceGenerator(name = "broadcast_emails_id_seq", sequenceName = "broadcast_emails_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "broadcast_emails_id_seq")
    private Long id;

    @Column(name = "name", nullable = false, length = 320)
    private String email;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at", nullable = true)
    private Date updatedAt;

    @Column(name = "deleted_at", nullable = true)
    private Date deletedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = new Date();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = new Date();
    }

    @ManyToMany
    @JoinTable(name = "broadcast_emails_broadcast_lists", joinColumns = @JoinColumn(name = "broadcast_emails_id"), inverseJoinColumns = @JoinColumn(name = "broadcast_lists_id"))
    private Set<BroadcastList> broadcastLists;
}
