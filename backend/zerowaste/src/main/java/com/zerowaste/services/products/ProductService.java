package com.zerowaste.services.products;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zerowaste.models.product.Product;
import com.zerowaste.repositories.ProductsRepository;

@Service
public class ProductService {
    
    @Autowired
    private ProductsRepository productsRepository;

    public List<Product> getAll () {
        return productsRepository.findAll();
    }
    
}
