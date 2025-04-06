package com.zerowaste.services.broadcasts;

import java.util.List;

import org.springframework.stereotype.Service;

import com.zerowaste.dtos.broadcasts.CreateBroadcastListDTO;
import com.zerowaste.models.broadcast.BroadcastEmail;
import com.zerowaste.models.broadcast.BroadcastList;
import com.zerowaste.models.broadcast.BroadcastListSendProtocol;
import com.zerowaste.models.broadcast.BroadcastListSendType;
import com.zerowaste.repositories.BroadcastEmailsRepository;
import com.zerowaste.repositories.BroadcastListsRepository;
import com.zerowaste.repositories.ProductsRepository;
import com.zerowaste.services.broadcasts.exceptions.BroadcastListProductsNotFoundException;

import jakarta.transaction.Transactional;

@Service
public class CreateBroadcastListService {
    private final BroadcastListsRepository broadcastListRepository;
    private final BroadcastEmailsRepository broadcastEmailsRepository;
    private final ProductsRepository productsRepository;

    public CreateBroadcastListService(
        BroadcastListsRepository broadcastListRepository,
        BroadcastEmailsRepository broadcastEmailsRepository,
        ProductsRepository productsRepository
    ) {
        this.broadcastListRepository = broadcastListRepository;
        this.broadcastEmailsRepository = broadcastEmailsRepository;
        this.productsRepository = productsRepository;
    }

    @Transactional
    public void execute(CreateBroadcastListDTO dto) throws BroadcastListProductsNotFoundException {
        var products = productsRepository.findAllById(dto.productsIds());

        if (products.size() != dto.productsIds().size()) {
            List<String> notFoundProducts = dto.productsIds().stream()
                .filter(id -> products.stream().noneMatch(product -> product.getId().equals(id)))
                .map(String::valueOf)
                .toList();

            throw new BroadcastListProductsNotFoundException(notFoundProducts);
        }

        var broadcastEmails = broadcastEmailsRepository.findAllByEmailIn(dto.emails());

        List<BroadcastEmail> notFoundBroadcastEmails = List.of();

        if (broadcastEmails.size() != dto.emails().size()) {
             notFoundBroadcastEmails = dto.emails().stream()
                .filter(email -> broadcastEmails.stream().noneMatch(broadcastEmail -> broadcastEmail.getEmail().equals(email)))
                .map(BroadcastEmail::new)
                .toList();

            broadcastEmailsRepository.saveAll(notFoundBroadcastEmails);
        }

        var broadcastList = new BroadcastList();

        broadcastEmails.addAll(notFoundBroadcastEmails);
        
        broadcastList.setName(dto.name());
        broadcastList.setDescription(dto.description());
        broadcastList.setSendType(BroadcastListSendType.valueOf(dto.sendType()));
        broadcastList.setSendProtocol(BroadcastListSendProtocol.EMAIL);
        broadcastList.setBroadcastEmails(broadcastEmails);
        broadcastList.setProducts(products);

        broadcastListRepository.save(broadcastList);
    }
}
