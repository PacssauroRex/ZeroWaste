package com.zerowaste.services.products;

import com.zerowaste.models.product.Product;
import com.zerowaste.repositories.ProductsRepository;
import com.zerowaste.services.products.exceptions.ProductNotFoundException;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class GetProductIdService {

    private ProductsRepository productsRepository;

    public GetProductIdService(ProductsRepository productsRepository) {
        this.productsRepository = productsRepository;
    }

    public Product execute (Long id) throws ProductNotFoundException {
        Optional<Product> p = productsRepository.findById(id);
        if(!p.isPresent() || p.get().getDeletedAt() != null) 
            throw new ProductNotFoundException("Produto não encontrado");

        return p.get();
    }
}
