package com.zerowaste.models.broadcast;

import java.time.LocalDate;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Table(name = "broadcast_lists")
@Entity(name = "broadcast_lists")
public class BroadcastList {
    public BroadcastList() {}

    public BroadcastList(
        Long id,
        String name,
        LocalDate createdAt,
        LocalDate updatedAt,
        LocalDate deletedAt,
        BroadcastListSendProtocol sendProtocol
    ) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
        this.sendProtocol = sendProtocol;
    }

    @Id
    @SequenceGenerator(name = "broadcast_lists_id_seq", sequenceName = "broadcast_lists_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "broadcast_lists_id_seq")
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "updated_at", nullable = true)
    private LocalDate updatedAt;

    @Column(name = "deleted_at", nullable = true)
    private LocalDate deletedAt;

    @Column(name = "send_protocol", nullable = false)
    @Enumerated(EnumType.STRING)
    private BroadcastListSendProtocol sendProtocol;

    @ManyToMany
    @JoinTable(name = "broadcast_emails_broadcast_lists", joinColumns = @JoinColumn(name = "broadcast_lists_id"), inverseJoinColumns = @JoinColumn(name = "broadcast_emails_id"))
    private Set<BroadcastEmail> broadcastEmails;
}

