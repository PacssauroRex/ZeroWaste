package com.zerowaste.services.products;

import com.zerowaste.models.product.Product;
import com.zerowaste.repositories.ProductsRepository;
import com.zerowaste.services.products.exceptions.ProductNotFoundException;
import java.time.LocalDate;
import org.springframework.stereotype.Service;

@Service
public class DeleteProductService {
    
    private final ProductsRepository productsRepository;

    public DeleteProductService(ProductsRepository productsRepository) {
        this.productsRepository = productsRepository;
    }

    public void execute (Long id) throws ProductNotFoundException {
        Product p = productsRepository.findById(id).get();

        if(p == null || p.getDeletedAt() != null) {
            throw new ProductNotFoundException("Produto não encontrado");
        }

        p.setDeletedAt(LocalDate.now());
        productsRepository.save(p);
    }
}
