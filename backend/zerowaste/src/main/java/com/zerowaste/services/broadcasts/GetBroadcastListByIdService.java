package com.zerowaste.services.broadcasts;

import org.springframework.stereotype.Service;
import com.zerowaste.dtos.broadcasts.GetBroadcastDTO;
import com.zerowaste.models.broadcast.BroadcastList;
import com.zerowaste.models.product.Product;
import com.zerowaste.repositories.BroadcastListsRepository;
import com.zerowaste.services.broadcasts.errors.BroadcastListNotFoundException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GetBroadcastListByIdService {

    private final BroadcastListsRepository broadcastListRepository;

    public GetBroadcastListByIdService(BroadcastListsRepository broadcastListRepository) {
        this.broadcastListRepository = broadcastListRepository;
    }

    public GetBroadcastDTO execute(Long id) {

        List<BroadcastList> broadcastLists = broadcastListRepository.findAllByDeletedAtIsNull();

        BroadcastList broadcastList = broadcastLists.stream()
            .filter(list -> list.getId().equals(id))
            .findFirst()
            .orElseThrow(() -> new BroadcastListNotFoundException("Lista de transmissão não encontrada para o ID: " + id));

        if (broadcastList.getDeletedAt() != null) {
            throw new BroadcastListNotFoundException("Lista de transmissão deletada não encontrada para o ID: " + id);
        }

        List<String> emails = broadcastList.getBroadcastEmails().stream()
            .map(emailObj -> emailObj.getEmail())
            .collect(Collectors.toList());

        Set<Long> broadcastListIds = broadcastList.getBroadcastEmails().stream()
            .map(emailObj -> emailObj.getId())
            .collect(Collectors.toSet());

        List<Long> productsIds = broadcastList.getProducts().stream()
            .map(p -> (Product) p)
            .map(Product::getId)
            .collect(Collectors.toList());

        return new GetBroadcastDTO(
            broadcastList.getId(),
            emails,
            broadcastList.getName(),
            broadcastList.getDescription(),
            broadcastList.getSendType().name(),
            broadcastList.getCreatedAt(),
            broadcastList.getUpdatedAt(),
            broadcastList.getDeletedAt(),
            broadcastListIds,
            productsIds
        );
    }
}









