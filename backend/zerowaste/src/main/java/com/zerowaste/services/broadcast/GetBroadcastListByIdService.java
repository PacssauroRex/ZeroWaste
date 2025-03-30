package com.zerowaste.services.broadcast;

import org.springframework.stereotype.Service;

import com.zerowaste.dtos.broadcast.BroadcastDTO;
import com.zerowaste.models.broadcast.BroadcastList;
import com.zerowaste.repositories.BroadcastListRepository;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GetBroadcastListByIdService {

    private final BroadcastListRepository broadcastListRepository;

    public GetBroadcastListByIdService(BroadcastListRepository broadcastListRepository) {
        this.broadcastListRepository = broadcastListRepository;
    }

    public BroadcastDTO execute(Long id) {
        Optional<BroadcastList> broadcastListOptional = broadcastListRepository.findById(id);

        if (!broadcastListOptional.isPresent()) {
            throw new RuntimeException("Lista de transmissão não encontrada");
        }

        BroadcastList broadcastList = broadcastListOptional.get();
        
        // A lógica para mapear os e-mails para um Set de String
        Set<String> emails = broadcastList.getBroadcastEmails().stream()
            .map(email -> email.getEmail())  // Assuming `getEmail()` returns a String
            .collect(Collectors.toSet());

        // Agora criando o DTO com a lista de e-mails corretamente
        return new BroadcastDTO(
            broadcastList.getId(),
            emails,  // Aqui passamos o Set de e-mails
            broadcastList.getName(),
            broadcastList.getCreatedAt(),
            broadcastList.getUpdatedAt(),
            broadcastList.getDeletedAt(),
            broadcastList.getBroadcastEmails().stream()
                .map(email -> email.getId())  // Converte os e-mails para IDs
                .collect(Collectors.toSet())  // Set de IDs
        );
    }
}


