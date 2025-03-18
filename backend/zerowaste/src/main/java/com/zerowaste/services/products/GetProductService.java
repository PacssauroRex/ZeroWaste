package com.zerowaste.services.products;

import com.zerowaste.dtos.products.GetProductsDTO;
import com.zerowaste.models.product.Product;
import com.zerowaste.repositories.ProductsRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class GetProductService {

    private final ProductsRepository productsRepository;

    public GetProductService(ProductsRepository productsRepository) {
        this.productsRepository = productsRepository;
    }

    public List<Product> execute (GetProductsDTO dto) {
        return productsRepository.findAllNotDeleted(dto.daysToExpire());
    }
}
