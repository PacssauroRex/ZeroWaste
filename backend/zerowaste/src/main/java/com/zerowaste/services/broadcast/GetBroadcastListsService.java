package com.zerowaste.services.broadcast;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.zerowaste.dtos.broadcast.BroadcastDTO;
import com.zerowaste.models.broadcast.BroadcastList;
import com.zerowaste.repositories.BroadcastListRepository;

@Service
public class GetBroadcastListsService {

    private final BroadcastListRepository broadcastListRepository;

    public GetBroadcastListsService(BroadcastListRepository broadcastListRepository) {
        this.broadcastListRepository = broadcastListRepository;
    }

    public List<BroadcastDTO> execute() {
        List<BroadcastList> broadcastLists = broadcastListRepository.findAll();
        List<BroadcastDTO> broadcastListDTOs = new ArrayList<>();

        for (BroadcastList broadcastList : broadcastLists) {
            // Pegando um único e-mail, ou você pode escolher outro critério para esse e-mail
            String email = broadcastList.getBroadcastEmails().stream()
                .map(emailObj -> emailObj.getEmail()) // Pega o primeiro e-mail
                .findFirst() // Pega o primeiro, ou você pode adaptar isso
                .orElse("no-email@example.com"); // Valor default caso não haja e-mails

            // Criando o DTO com as informações corretas
            BroadcastDTO dto = new BroadcastDTO(
                broadcastList.getId(),
                email, // Um único e-mail
                broadcastList.getName(),
                broadcastList.getCreatedAt(),
                broadcastList.getUpdatedAt(),
                broadcastList.getDeletedAt(),
                broadcastList.getBroadcastEmails().stream()
                    .map(emailObj -> emailObj.getId())
                    .collect(Collectors.toSet()) // Convertendo os e-mails para IDs
            );
            broadcastListDTOs.add(dto);
        }

        return broadcastListDTOs;
    }
}




