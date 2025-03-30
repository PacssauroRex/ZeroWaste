package com.zerowaste.services.broadcast;

import org.springframework.stereotype.Service;

import com.zerowaste.dtos.broadcast.UpdateBroadcastDTO;
import com.zerowaste.models.broadcast.BroadcastList;
import com.zerowaste.repositories.BroadcastListRepository;

import java.util.Optional;

@Service
public class UpdateBroadcastListService {

    private final BroadcastListRepository broadcastListRepository;

    public UpdateBroadcastListService(BroadcastListRepository broadcastListRepository) {
        this.broadcastListRepository = broadcastListRepository;
    }

    public void execute(Long id, UpdateBroadcastDTO dto) {
        Optional<BroadcastList> broadcastListOptional = broadcastListRepository.findById(id);

        if (!broadcastListOptional.isPresent()) {
            throw new RuntimeException("Lista de transmissão não encontrada");
        }

        BroadcastList broadcastList = broadcastListOptional.get();
        broadcastList.setName(dto.name());

        broadcastListRepository.save(broadcastList);
    }
}
