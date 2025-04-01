package com.zerowaste.services.broadcasts;

import java.util.List;

import org.springframework.stereotype.Service;

import com.zerowaste.dtos.broadcasts.UpdateBroadcastListDTO;
import com.zerowaste.models.broadcast.BroadcastEmail;
import com.zerowaste.models.broadcast.BroadcastListSendProtocol;
import com.zerowaste.models.broadcast.BroadcastListSendType;
import com.zerowaste.repositories.BroadcastEmailsRepository;
import com.zerowaste.repositories.BroadcastListsRepository;
import com.zerowaste.repositories.ProductsRepository;
import com.zerowaste.services.broadcasts.exceptions.BroadcastListNotFoundException;
import com.zerowaste.services.broadcasts.exceptions.BroadcastListProductsNotFoundException;

import jakarta.transaction.Transactional;

@Service
public class UpdateBroadcastListService {
    private BroadcastListsRepository broadcastListRepository;
    private BroadcastEmailsRepository broadcastEmailsRepository;
    private ProductsRepository productsRepository;

    public UpdateBroadcastListService(
        BroadcastListsRepository broadcastListRepository,
        BroadcastEmailsRepository broadcastEmailsRepository,
        ProductsRepository productsRepository
    ) {
        this.broadcastListRepository = broadcastListRepository;
        this.broadcastEmailsRepository = broadcastEmailsRepository;
        this.productsRepository = productsRepository;
    }

    @Transactional
    public void execute(Long id, UpdateBroadcastListDTO dto) throws BroadcastListProductsNotFoundException {
        var broadcastList = broadcastListRepository.findById(id).orElse(null);

        if (broadcastList == null) {
            throw new BroadcastListNotFoundException("Lista de transmissão #" + id + " não encontrada");
        }
        
        var products = productsRepository.findAllById(dto.productsIds());

        if (products.size() != dto.productsIds().size()) {
            List<String> notFoundProducts = dto.productsIds().stream()
                .filter(productId -> products.stream().noneMatch(product -> product.getId().equals(productId)))
                .map(String::valueOf)
                .toList();

            throw new BroadcastListProductsNotFoundException(notFoundProducts);
        }

        var broadcastEmails = broadcastEmailsRepository.findAllByEmailIn(dto.emails());

        if (broadcastEmails.size() != dto.emails().size()) {
            List<BroadcastEmail> broadcastEmailsToCreate = dto.emails().stream()
                .filter(email -> broadcastEmails.stream().noneMatch(broadcastEmail -> broadcastEmail.getEmail().equals(email)))
                .map(BroadcastEmail::new)
                .toList();

            broadcastEmailsRepository.saveAll(broadcastEmailsToCreate);
            broadcastEmails.addAll(broadcastEmailsToCreate);

            List<BroadcastEmail> broadcastEmailsToRemove = broadcastEmails.stream()
                .filter(broadcastEmail -> dto.emails().stream().noneMatch(email -> email.equals(broadcastEmail.getEmail())))
                .toList();
            
            broadcastEmails.removeAll(broadcastEmailsToRemove);
        }
        
        
        broadcastList.setName(dto.name());
        broadcastList.setSendType(BroadcastListSendType.valueOf(dto.sendType()));
        broadcastList.setSendProtocol(BroadcastListSendProtocol.EMAIL);
        broadcastList.setBroadcastEmails(broadcastEmails);
        broadcastList.setProducts(products);

        broadcastListRepository.save(broadcastList);
    }
}
