package com.zerowaste.services.products;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zerowaste.models.product.Product;
import com.zerowaste.repositories.ProductsRepository;
import com.zerowaste.services.products.exceptions.ProductNotFoundException;

@Service
public class ProductService {
    
    @Autowired
    private ProductsRepository productsRepository;

    public List<Product> getAll () {
        return productsRepository.findAll();
    }
    
    public Product getById (Long id) throws ProductNotFoundException {
        Optional<Product> p = productsRepository.findById(id);
        if(!p.isPresent()) {
            throw new ProductNotFoundException("Produto não encontrado");
        }
        return p.get();
    }
}
