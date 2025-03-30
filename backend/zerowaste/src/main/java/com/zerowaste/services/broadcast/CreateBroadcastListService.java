package com.zerowaste.services.broadcast;

import org.springframework.stereotype.Service;

import com.zerowaste.dtos.broadcast.CreateBroadcastDTO;
import com.zerowaste.models.broadcast.BroadcastList;
import com.zerowaste.models.broadcast.BroadcastListSendProtocol;
import com.zerowaste.models.broadcast.BroadcastListSendType;
import com.zerowaste.repositories.BroadcastListRepository;

@Service
public class CreateBroadcastListService {

    private final BroadcastListRepository broadcastListRepository;

    public CreateBroadcastListService(BroadcastListRepository broadcastListRepository) {
        this.broadcastListRepository = broadcastListRepository;
    }

    public void execute(CreateBroadcastDTO dto) {
        BroadcastList broadcastList = new BroadcastList();
        broadcastList.setName(dto.name());
        broadcastList.setSendProtocol(BroadcastListSendProtocol.EMAIL);  // Usando o enum correto
        broadcastList.setSendType(BroadcastListSendType.MANUAL);  // Usando o enum correto
        
        // A lógica para associar os e-mails pode ser adicionada aqui.
        broadcastListRepository.save(broadcastList);
    }
    
}
