package com.zerowaste.services.broadcasts;

import org.springframework.stereotype.Service;

import com.zerowaste.dtos.broadcasts.GetBroadcastListByIdResponseBodyDTO;
import com.zerowaste.repositories.BroadcastListsRepository;
import com.zerowaste.services.broadcasts.exceptions.BroadcastListNotFoundException;

import java.util.Optional;


@Service
public class GetBroadcastListByIdService {

    private final BroadcastListsRepository broadcastListRepository;

    public GetBroadcastListByIdService(BroadcastListsRepository broadcastListRepository) {
        this.broadcastListRepository = broadcastListRepository;
    }

    public GetBroadcastListByIdResponseBodyDTO execute(Long id) {
        var broadcast = broadcastListRepository.findById(id).orElse(null);

        if (broadcast == null || broadcast.getDeletedAt() != null) {
            throw new BroadcastListNotFoundException("Lista de transmissão não encontrada");
        }
        
        var broadcastListDTO = new GetBroadcastListByIdResponseBodyDTO(
            broadcast.getId(),
            broadcast.getName(),
            broadcast.getDescription(),
            broadcast.getSendType(),
            broadcast.getBroadcastEmails().stream().map(email -> email.getEmail()).toArray(String[]::new),
            broadcast.getProducts().stream().map(product -> product.getId()).toArray(Long[]::new),
            broadcast.getCreatedAt() == null ? null : broadcast.getCreatedAt().toString(),
            broadcast.getUpdatedAt() == null ? null : broadcast.getUpdatedAt().toString(),
            Optional.ofNullable(broadcast.getDeletedAt()).map(Object::toString).orElse(null)
        );

        return broadcastListDTO;
    }
}









