package com.zerowaste.services.products;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zerowaste.dtos.products.EditProductDTO;
import com.zerowaste.models.product.ProductCategory;
import com.zerowaste.models.promotion.Promotion;
import com.zerowaste.repositories.ProductsRepository;
import com.zerowaste.repositories.PromotionsRepository;
import com.zerowaste.services.products.exceptions.ProductNotFoundException;

@Service
public class EditProductService {
    
    @Autowired
    private ProductsRepository productsRepository;

    @Autowired
    private PromotionsRepository promotionsRepository;

    public void execute (Long id, EditProductDTO dto) throws ProductNotFoundException {
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
}
