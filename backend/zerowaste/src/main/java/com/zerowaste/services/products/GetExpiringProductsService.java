package com.zerowaste.services.products;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.zerowaste.repositories.ProductsRepository;

@Service
public class GetExpiringProductsService {
    
    private final ProductsRepository productsRepository;

    public GetExpiringProductsService (ProductsRepository productsRepository) {
        this.productsRepository = productsRepository;
    }

    public String execute() {
        Long count = productsRepository.countExpiringProducts(
            LocalDate.now()
            .plusDays(10)
            .toString());

        return Long.toString(count);
    }
}
