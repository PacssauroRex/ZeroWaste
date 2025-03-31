package com.zerowaste.services.broadcasts;

import org.springframework.stereotype.Service;
import com.zerowaste.models.broadcast.BroadcastList;
import com.zerowaste.repositories.BroadcastListsRepository;
import com.zerowaste.services.broadcasts.exceptions.BroadcastListNotFoundException;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class DeleteBroadcastListService {

    private final BroadcastListsRepository broadcastListsRepository;

    public DeleteBroadcastListService(BroadcastListsRepository broadcastListsRepository) {
        this.broadcastListsRepository = broadcastListsRepository;
    }

    public void execute(Long id) {
        Optional<BroadcastList> broadcastOpt = broadcastListsRepository.findById(id);

        if(!broadcastOpt.isPresent() || broadcastOpt.get().getDeletedAt() != null)
            throw new BroadcastListNotFoundException(null);

        BroadcastList broadcast = broadcastOpt.get();
        
        broadcast.setDeletedAt(LocalDate.now());

        broadcastListsRepository.save(broadcast);
    }
}



