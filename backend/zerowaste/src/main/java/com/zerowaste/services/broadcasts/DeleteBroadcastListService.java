package com.zerowaste.services.broadcasts;

import org.springframework.stereotype.Service;
import com.zerowaste.models.broadcast.BroadcastList;
import com.zerowaste.repositories.BroadcastListsRepository;
import com.zerowaste.services.broadcasts.exceptions.BroadcastListNotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class DeleteBroadcastListService {

    private final BroadcastListsRepository broadcastListsRepository;

    public DeleteBroadcastListService(BroadcastListsRepository broadcastListsRepository) {
        this.broadcastListsRepository = broadcastListsRepository;
    }

    public void execute(Long id) {
        List<BroadcastList> broadcastLists = broadcastListsRepository.findAllNotDeleted();

        Optional<BroadcastList> broadcastListOptional = broadcastLists.stream()
            .filter(broadcastList -> broadcastList.getId().equals(id))
            .findFirst();

        if (broadcastListOptional.isEmpty()) {
            throw new BroadcastListNotFoundException("Lista de transmissão não encontrada para o ID: " + id);
        }

        BroadcastList broadcastList = broadcastListOptional.get();

        if (broadcastList.getDeletedAt() != null) {
            return;
        }

        broadcastList.setDeletedAt(LocalDate.now());
        broadcastListsRepository.save(broadcastList);
    }
}



