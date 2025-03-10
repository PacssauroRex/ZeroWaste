package com.zerowaste.services.products;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zerowaste.dtos.products.GetProductsDTO;
import com.zerowaste.models.product.Product;
import com.zerowaste.repositories.ProductsRepository;

@Service
public class GetProductService {
    @Autowired
    private ProductsRepository productsRepository;

    public List<Product> execute (GetProductsDTO dto) {
        return productsRepository.findAllNotDeleted(dto.daysToExpire());
    }
}
