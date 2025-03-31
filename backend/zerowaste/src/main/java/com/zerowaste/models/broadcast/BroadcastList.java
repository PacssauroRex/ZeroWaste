package com.zerowaste.models.broadcast;

import java.time.LocalDate;
import java.util.List;

import com.zerowaste.models.product.Product;

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
        BroadcastListSendProtocol sendProtocol,
        BroadcastListSendType sendType
    ) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
        this.sendProtocol = sendProtocol;
        this.sendType = sendType;
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

    @Column(name = "send_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private BroadcastListSendType sendType;

    @ManyToMany
    @JoinTable(name = "broadcast_emails_broadcast_lists", joinColumns = @JoinColumn(name = "broadcast_lists_id"), inverseJoinColumns = @JoinColumn(name = "broadcast_emails_id"))
    private List<BroadcastEmail> broadcastEmails;

    @ManyToMany
    @JoinTable(name = "broadcast_lists_products", joinColumns = @JoinColumn(name = "broadcast_lists_id"), inverseJoinColumns = @JoinColumn(name = "products_id"))
    private List<Product> products;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public BroadcastListSendProtocol getSendProtocol() {
        return sendProtocol;
    }

    public void setSendProtocol(BroadcastListSendProtocol sendProtocol) {
        this.sendProtocol = sendProtocol;
    }

    public BroadcastListSendType getSendType() {
        return sendType;
    }

    public void setSendType(BroadcastListSendType sendType) {
        this.sendType = sendType;
    }

    public List<BroadcastEmail> getBroadcastEmails() {
        return broadcastEmails;
    }

    public void setBroadcastEmails(List<BroadcastEmail> broadcastEmails) {
        this.broadcastEmails = broadcastEmails;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}

