package com.zerowaste.dtos.broadcasts;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public class GetBroadcastDTO {
    private Long id;
    private String name;
    private String sendType;
    private List<String> email;  
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private LocalDate deletedAt;

    public GetBroadcastDTO() {}

    public GetBroadcastDTO(Long id, List<String> emails, String name, String sendType,
                           LocalDate createdAt, LocalDate updatedAt, LocalDate deletedAt) {
        this.id = id;
        this.email = emails;
        this.name = name;
        this.sendType = sendType;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
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


