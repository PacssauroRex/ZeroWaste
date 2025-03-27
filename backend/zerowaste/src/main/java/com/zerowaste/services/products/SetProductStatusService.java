package com.zerowaste.services.products;

import java.util.Optional;
import org.springframework.stereotype.Service;

import com.zerowaste.models.product.Product;
import com.zerowaste.models.product.ProductStatus;
import com.zerowaste.repositories.ProductsRepository;
import com.zerowaste.services.products.exceptions.ProductNotAvaliableException;
import com.zerowaste.services.products.exceptions.ProductNotFoundException;

@Service
public class SetProductStatusService {

    private final ProductsRepository productsRepository;

    public SetProductStatusService (ProductsRepository productsRepository) {
        this.productsRepository = productsRepository;
    }

    public void execute (Long id, ProductStatus status) throws ProductNotFoundException, ProductNotAvaliableException {
        Optional<Product> productOpt = productsRepository.findById(id);

        if(!productOpt.isPresent())
            throw new ProductNotFoundException("Produto não encontrado!");
        
        if (productOpt.get().getStatus() != ProductStatus.AVALIABLE)
            throw new ProductNotAvaliableException("Produto não disponível");

        Product product = productOpt.get();

        product.setStatus(status);

        productsRepository.save(product);
    }
}
