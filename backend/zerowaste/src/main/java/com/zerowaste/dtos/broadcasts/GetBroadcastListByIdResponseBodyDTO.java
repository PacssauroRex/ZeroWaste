package com.zerowaste.dtos.broadcasts;

import com.zerowaste.models.broadcast.BroadcastListSendType;

public record GetBroadcastListByIdResponseBodyDTO(
        Long id,
        String name,
        String description,
        BroadcastListSendType sendType,
        String[] emails,
        Long[] productsIds,
        String createdAt,
        String updatedAt,
        String deletedAt
) {
    public GetBroadcastListByIdResponseBodyDTO(
            Long id,
            String name,
            String description,
            BroadcastListSendType sendType,
            String[] emails,
            Long[] productsIds,
            String createdAt,
            String updatedAt,
            String deletedAt
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.sendType = sendType;
        this.emails = emails;
        this.productsIds = productsIds;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

}
