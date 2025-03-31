package com.zerowaste.services.broadcasts;

import org.springframework.stereotype.Service;
import com.zerowaste.models.broadcast.BroadcastList;
import com.zerowaste.repositories.BroadcastListsRepository;
import com.zerowaste.services.broadcasts.exceptions.BroadcastListNotFoundException;

import java.util.Optional;


@Service
public class GetBroadcastListByIdService {

    private final BroadcastListsRepository broadcastListRepository;

    public GetBroadcastListByIdService(BroadcastListsRepository broadcastListRepository) {
        this.broadcastListRepository = broadcastListRepository;
    }

    public BroadcastList execute(Long id) {
        Optional<BroadcastList> broadcastOpt = broadcastListRepository.findById(id);

        if(!broadcastOpt.isPresent() || broadcastOpt.get().getDeletedAt() != null)
            throw new BroadcastListNotFoundException("Lista de transmissão não encontrada");
        
        return broadcastOpt.get();
    }
}









