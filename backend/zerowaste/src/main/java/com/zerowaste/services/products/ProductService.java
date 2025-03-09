package com.zerowaste.services.products;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zerowaste.dtos.products.EditProductDTO;
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

        validateProduct(id, dto);

        Product p = productsRepository.findById(id).get();
        
        p.setName(dto.name());
        p.setDescription(dto.description());
        p.setBrand(dto.brand());
        p.setCategory(ProductCategory.valueOf(dto.category()));
        p.setUnitPrice(dto.unitPrice());
        p.setPromotionPrice(dto.promotionPrice());
        p.setExpiresAt(dto.expiresAt());
        p.setStock(dto.stock());
        p.setUpdatedAt(LocalDate.now());
    
        
        if (dto.promotionsIds() != null && !dto.promotionsIds().isEmpty()) {
            Set<Promotion> promotions = promotionsRepository.findAllById(dto.promotionsIds())
                                                        .stream()
                                                        .collect(Collectors.toSet());
            p.setPromotions(promotions);
        }

        productsRepository.save(p);
    }

    private void validateProduct(Long id, EditProductDTO dto) throws ProductNotFoundException {

        Product p = productsRepository.findById(id)
            .orElseThrow(() -> new ProductNotFoundException("Produto não encontrado"));


        if (p.getDeletedAt() != null) {
            throw new ProductNotFoundException("Produto foi deletado");
        }   

    public void validatePromotions(EditProductDTO dto) {
        if (dto.promotionsIds() != null && !dto.promotionsIds().isEmpty()) {
            
            List<Long> invalidIds = dto.promotionsIds().stream()
                .filter(id -> !promotionsRepository.existsById(id)) 
                .collect(Collectors.toList());
                
            if (!invalidIds.isEmpty()) {
                throw new IllegalArgumentException("Um ou mais IDs de promoção são inválidos: " + invalidIds);
            }
        }
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