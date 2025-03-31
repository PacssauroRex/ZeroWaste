package com.zerowaste.dtos.broadcasts;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public class GetBroadcastDTO {
    private Long id;
    private String name;
    private String description;
    private String sendType;
    private List<String> email;  
    private List<Long> productsIds;
    private Set<Long> broadcastListIds;  
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private LocalDate deletedAt;

    public GetBroadcastDTO() {}

    public GetBroadcastDTO(Long id, List<String> emails, String name, String description, String sendType,
                           LocalDate createdAt, LocalDate updatedAt, LocalDate deletedAt,
                           Set<Long> broadcastListIds, List<Long> productsIds) {
        this.id = id;
        this.email = emails;
        this.name = name;
        this.description = description;
        this.sendType = sendType;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
        this.broadcastListIds = broadcastListIds;
        this.productsIds = productsIds != null ? productsIds : List.of();
    }

    // Getters e Setters

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSendType() {
        return sendType;
    }

    public void setSendType(String sendType) {
        this.sendType = sendType;
    }

    public List<String> getEmail() {
        return email;
    }

    public void setEmail(List<String> email) {
        this.email = email;
    }

    public List<Long> getProductsIds() {
        return productsIds;
    }

    public void setProductsIds(List<Long> productsIds) {
        this.productsIds = productsIds;
    }

    public Set<Long> getBroadcastListIds() {
        return broadcastListIds;
    }

    public void setBroadcastListIds(Set<Long> broadcastListIds) {
        this.broadcastListIds = broadcastListIds;
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
}


