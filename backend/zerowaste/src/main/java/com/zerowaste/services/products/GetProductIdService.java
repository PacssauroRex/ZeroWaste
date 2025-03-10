package com.zerowaste.services.products;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zerowaste.models.product.Product;
import com.zerowaste.repositories.ProductsRepository;
import com.zerowaste.services.products.exceptions.ProductNotFoundException;

@Service
public class GetProductIdService {
    @Autowired
    private ProductsRepository productsRepository;

    public Product execute (Long id) throws ProductNotFoundException {
        Optional<Product> p = productsRepository.findById(id);
        if(!p.isPresent() || p.get().getDeletedAt() != null) 
            throw new ProductNotFoundException("Produto não encontrado");

        return p.get();
    }
}
