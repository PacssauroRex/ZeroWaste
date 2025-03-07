package com.zerowaste.services.products;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zerowaste.dtos.products.EditProductDTO;
import com.zerowaste.models.product.Product;
import com.zerowaste.models.product.ProductCategory;
import com.zerowaste.repositories.ProductsRepository;
import com.zerowaste.services.products.exceptions.ProductNotFoundException;

@Service
public class ProductService {
    
    @Autowired
    private ProductsRepository productsRepository;

    public List<Product> getAll () {
        return productsRepository.findAllNotDeleted();
    }
    
    public Product getById (Long id) throws ProductNotFoundException {
        Optional<Product> p = productsRepository.findById(id);
        if(!p.isPresent() || p.get().getDeletedAt() != null) 
            throw new ProductNotFoundException("Produto não encontrado");

        return p.get();
    }

    public void edit (Long id, EditProductDTO dto) throws ProductNotFoundException {
        Product p = productsRepository.findById(id).get();
        
        if(p == null || p.getDeletedAt() != null)
            throw new ProductNotFoundException("Produto não encontrado");
        
        p.setName(dto.name());
        p.setDescription(dto.description());
        p.setBrand(dto.brand());
        p.setCategory(ProductCategory.valueOf(dto.category()));
        p.setUnitPrice(dto.unitPrice());
        p.setPromotionPrice(dto.promotionPrice());
        p.setExpiresAt(dto.expiresAt());
        p.setStock(dto.stock());
        p.setUpdatedAt(LocalDate.now());

        productsRepository.save(p);
    }

    public void delete (Long id) throws ProductNotFoundException {
        Product p = productsRepository.findById(id).get();

        if(p == null || p.getDeletedAt() != null)
            throw new ProductNotFoundException("Produto não encontrado");

        p.setDeletedAt(LocalDate.now());
        
        productsRepository.save(p);
    }
}
