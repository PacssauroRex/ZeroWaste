package com.zerowaste.services.broadcasts;

import org.springframework.stereotype.Service;
import com.zerowaste.dtos.broadcasts.GetBroadcastDTO;
import com.zerowaste.models.broadcast.BroadcastList;
import com.zerowaste.models.product.Product;
import com.zerowaste.repositories.BroadcastListsRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
            .collect(Collectors.toList());

        List<Long> productIds = (broadcastList.getProducts() != null) ? 
            broadcastList.getProducts().stream()
                .map(p -> (Product) p)
                .map(Product::getId)
                .collect(Collectors.toList()) :
            new ArrayList<>();

        return new GetBroadcastDTO(
            broadcastList.getId(),
            emails,
            broadcastList.getName(),
            broadcastList.getDescription(),
            broadcastList.getSendType().name(),
            broadcastList.getCreatedAt(),
            broadcastList.getUpdatedAt(),
            broadcastList.getDeletedAt(),
            broadcastList.getBroadcastEmails().stream()
                .map(emailObj -> emailObj.getId())
                .collect(Collectors.toSet()),
            productIds
        );
    }).collect(Collectors.toList());
    }

}
    