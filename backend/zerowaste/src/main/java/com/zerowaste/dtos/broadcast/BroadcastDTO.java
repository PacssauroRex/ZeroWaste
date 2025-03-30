package com.zerowaste.dtos.broadcast;

import java.time.LocalDate;
import java.util.Set;

public record BroadcastDTO(
    Long id,
    String email,  
    String name,
    LocalDate createdAt,
    LocalDate updatedAt,
    LocalDate deletedAt,
    Set<Long> broadcastListIds  
) {}
