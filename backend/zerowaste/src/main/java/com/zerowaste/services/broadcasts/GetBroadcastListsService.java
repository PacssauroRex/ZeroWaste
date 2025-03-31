package com.zerowaste.services.broadcasts;

import org.springframework.stereotype.Service;
import com.zerowaste.dtos.broadcasts.GetBroadcastDTO;
import com.zerowaste.models.broadcast.BroadcastList;
import com.zerowaste.repositories.BroadcastListsRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetBroadcastListsService {

    private final BroadcastListsRepository broadcastListsRepository;

    public GetBroadcastListsService(BroadcastListsRepository broadcastListsRepository) {
        this.broadcastListsRepository = broadcastListsRepository;
    }

    public List<GetBroadcastDTO> execute() {
        List<BroadcastList> broadcastLists = broadcastListsRepository.findAllNotDeleted();
        List<GetBroadcastDTO> returnList = new ArrayList<>();
        for (var broadcastList : broadcastLists) {
            returnList.add(
                new GetBroadcastDTO(
                    broadcastList.getId(), 
                    broadcastList.getName(), 
                    broadcastList.getSendType().value()
                )
            );
        }
        return returnList;
    }

}
    