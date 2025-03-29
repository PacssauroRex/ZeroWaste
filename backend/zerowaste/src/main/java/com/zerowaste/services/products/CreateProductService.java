package com.zerowaste.services.products;

import com.zerowaste.dtos.products.CreateProductDTO;
import com.zerowaste.models.product.Product;
import com.zerowaste.models.product.ProductCategory;
import com.zerowaste.models.product.ProductStatus;
import com.zerowaste.repositories.ProductsRepository;
import org.springframework.stereotype.Service;

@Service
public class CreateProductService {
    
    private ProductsRepository productsRepository;

    public CreateProductService(ProductsRepository productsRepository) {
        this.productsRepository = productsRepository;
    }
    
    public void execute(CreateProductDTO dto) {
        var product = new Product();

        product.setName(dto.name());
        product.setDescription(dto.description());
        product.setBrand(dto.brand());
        product.setCategory(ProductCategory.valueOf(dto.category()));
        product.setUnitPrice(dto.unitPrice());
        product.setStock(dto.stock());
        product.setExpiresAt(dto.expiresAt());
        product.setStatus(ProductStatus.AVALIABLE);

        productsRepository.save(product);
    }
}
