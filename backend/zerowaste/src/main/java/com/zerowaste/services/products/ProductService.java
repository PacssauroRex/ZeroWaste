package com.zerowaste.services.products;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zerowaste.dtos.EditProductDTO;
import com.zerowaste.models.product.Product;
import com.zerowaste.models.product.ProductCategory;
import com.zerowaste.repositories.ProductsRepository;
import com.zerowaste.services.products.exceptions.ProductDeletedException;
import com.zerowaste.services.products.exceptions.ProductNotFoundException;

@Service
public class ProductService {
    
    @Autowired
    private ProductsRepository productsRepository;

    public List<Product> getAll () {
        return productsRepository.findAll();
    }
    
    public Product getById (Long id) throws ProductNotFoundException, ProductDeletedException {
        Optional<Product> p = productsRepository.findById(id);
        if(!p.isPresent()) 
            throw new ProductNotFoundException("Produto não encontrado");
        
        if(p.get().getDeletedAt() == null)
            throw new ProductDeletedException("O produto em questão foi deletado");

        return p.get();
    }

    public void edit (Long id, EditProductDTO dto) throws ProductNotFoundException, ProductDeletedException {
        Product p = productsRepository.findById(id).get();
        
        if(p == null)
            throw new ProductNotFoundException("Produto não encontrado");

        if(p.getDeletedAt() != null)
            throw new ProductDeletedException("O produto em questão foi deletado!");
        
        p.setName(dto.name());
        p.setDescription(dto.description());
        p.setBrand(dto.brand());
        p.setCategory(ProductCategory.valueOf(dto.category()));
        p.setUnitPrice(dto.unitPrice());
        p.setPromotionPrice(dto.promotionPrice());
        p.setExpiresAt(dto.expiresAt());
        p.setStock(dto.stock());
        p.setUpdatedAt(new Date());

        productsRepository.save(p);
    }
}
