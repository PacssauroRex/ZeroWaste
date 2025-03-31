package com.zerowaste.services.broadcasts;

import org.springframework.stereotype.Service;
import com.zerowaste.dtos.broadcasts.GetBroadcastDTO;
import com.zerowaste.models.broadcast.BroadcastList;
import com.zerowaste.repositories.BroadcastListsRepository;

import java.util.List;

@Service
public class GetBroadcastListsService {

    private final BroadcastListsRepository broadcastListsRepository;

    public GetBroadcastListsService(BroadcastListsRepository broadcastListsRepository) {
        this.broadcastListsRepository = broadcastListsRepository;
    }

    public List<GetBroadcastDTO> execute() {
    List<BroadcastList> broadcastLists = broadcastListsRepository.findAllByDeletedAtIsNull();
    
    return broadcastLists.stream().map(broadcastList -> {
    
        List<String> emails = broadcastList.getBroadcastEmails().stream()
            .map(emailObj -> emailObj.getEmail())
            .toList();

        return new GetBroadcastDTO(
            broadcastList.getId(),
            emails,
            broadcastList.getName(),
            broadcastList.getSendType().name(),
            broadcastList.getCreatedAt(),
            broadcastList.getUpdatedAt(),
            broadcastList.getDeletedAt()
        );
    }).toList();
    }

}
    