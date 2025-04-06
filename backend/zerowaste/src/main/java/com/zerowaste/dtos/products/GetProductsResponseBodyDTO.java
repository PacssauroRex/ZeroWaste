package com.zerowaste.dtos.products;

import java.time.LocalDate;

import com.zerowaste.models.product.ProductCategory;
import com.zerowaste.models.product.ProductStatus;

public record GetProductsResponseBodyDTO(
    Long id,
    String name,
    String description,
    String brand,
    ProductCategory category,
    Double unitPrice,
    Double promotionPrice,
    Integer stock,
    LocalDate expiresAt,
    ProductStatus status
) {
    public GetProductsResponseBodyDTO(
        Long id,
        String name,
        String description,
        String brand,
        String category,
        Double unitPrice,
        Double promotionPrice,
        Integer stock,
        LocalDate expiresAt,
        String status
    ) {
        this(id, name, description, brand, ProductCategory.valueOf(category), unitPrice, promotionPrice, stock, expiresAt, ProductStatus.valueOf(status));
    }
}