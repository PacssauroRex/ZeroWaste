package com.zerowaste.services.products;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zerowaste.dtos.products.EditProductDTO;
import com.zerowaste.dtos.products.GetProductsDTO;
import com.zerowaste.models.product.Product;
import com.zerowaste.models.product.ProductCategory;
import com.zerowaste.models.promotion.Promotion;
import com.zerowaste.repositories.ProductsRepository;
import com.zerowaste.repositories.PromotionsRepository;
import com.zerowaste.services.products.exceptions.ProductNotFoundException;

@Service
public class ProductService {
    
    @Autowired
    private ProductsRepository productsRepository;
  
    @Autowired
    private PromotionsRepository promotionsRepository;


    public List<Product> getAll (GetProductsDTO dto) {
        return productsRepository.findAllNotDeleted(dto.daysToExpire());
    }
    
    public Product getById (Long id) throws ProductNotFoundException {
        Optional<Product> p = productsRepository.findById(id);
        if(!p.isPresent() || p.get().getDeletedAt() != null) 
            throw new ProductNotFoundException("Produto não encontrado");

        return p.get();
    }

    public void edit (Long id, EditProductDTO dto) throws ProductNotFoundException {
        var product = productsRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Produto não encontrado"));
        
        if (product.getDeletedAt() != null) {
            throw new ProductNotFoundException("Produto não encontrado");
        }
        
        product.setName(dto.name());
        product.setDescription(dto.description());
        product.setBrand(dto.brand());
        product.setCategory(ProductCategory.valueOf(dto.category()));
        product.setUnitPrice(dto.unitPrice());
        product.setPromotionPrice(dto.promotionPrice());
        product.setExpiresAt(dto.expiresAt());
        product.setStock(dto.stock());
        product.setUpdatedAt(LocalDate.now());

        if (dto.promotionsIds() != null && !dto.promotionsIds().isEmpty()) {
            Set<Promotion> promotions = promotionsRepository.findAllById(dto.promotionsIds())
                                                        .stream()
                                                        .collect(Collectors.toSet());
            
            if (!promotions.isEmpty()) {
                throw new IllegalArgumentException("Um ou mais IDs de promoção são inválidos: " + promotions);
            }

            product.setPromotions(promotions);
        }

        productsRepository.save(product);
    }
  
    public void delete (Long id) throws ProductNotFoundException {
        Product p = productsRepository.findById(id).get();

        if(p == null || p.getDeletedAt() != null) {
            throw new ProductNotFoundException("Produto não encontrado");
        }

        p.setDeletedAt(LocalDate.now());
        productsRepository.save(p);
    }
}