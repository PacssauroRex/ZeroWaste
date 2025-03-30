package com.zerowaste.services.broadcast;

import org.springframework.stereotype.Service;
import com.zerowaste.models.broadcast.BroadcastList;
import com.zerowaste.repositories.BroadcastListRepository;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class DeleteBroadcastListService {

    private final BroadcastListRepository broadcastListRepository;

    public DeleteBroadcastListService(BroadcastListRepository broadcastListRepository) {
        this.broadcastListRepository = broadcastListRepository;
    }

    public void execute(Long id) {
        Optional<BroadcastList> broadcastListOptional = broadcastListRepository.findById(id);

        if (!broadcastListOptional.isPresent()) {
            throw new RuntimeException("Lista de transmissão não encontrada");
        }

        BroadcastList broadcastList = broadcastListOptional.get();

        // Atualiza a data de deletedAt
        broadcastList.setDeletedAt(LocalDate.now());

        // Salva a lista de transmissão atualizada
        broadcastListRepository.save(broadcastList);
    }
}

