package com.zerowaste.services.products;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zerowaste.models.product.Product;
import com.zerowaste.repositories.ProductsRepository;
import com.zerowaste.services.products.exceptions.ProductNotFoundException;

@Service
public class DeleteProductService {
    @Autowired
    private ProductsRepository productsRepository;

    public void execute (Long id) throws ProductNotFoundException {
        Product p = productsRepository.findById(id).get();

        if(p == null || p.getDeletedAt() != null) {
            throw new ProductNotFoundException("Produto não encontrado");
        }

        p.setDeletedAt(LocalDate.now());
        productsRepository.save(p);
    }
}
