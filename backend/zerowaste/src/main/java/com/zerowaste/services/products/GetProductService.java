package com.zerowaste.services.products;

import com.zerowaste.dtos.products.GetProductsRequestQueryDTO;
import com.zerowaste.dtos.products.GetProductsResponseBodyDTO;
import com.zerowaste.repositories.ProductsRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class GetProductService {

    private ProductsRepository productsRepository;

    public GetProductService(ProductsRepository productsRepository) {
        this.productsRepository = productsRepository;
    }

    public List<GetProductsResponseBodyDTO> execute (GetProductsRequestQueryDTO dto) {
        var products = productsRepository.findAllNotDeleted(dto.daysToExpire());

        var productsDTO = products.stream()
            .map(product -> new GetProductsResponseBodyDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getBrand(),
                product.getCategory().getCategory(),
                product.getUnitPrice(),
                product.getPromotionPrice(),
                product.getStock(),
                product.getExpiresAt(),
                product.getStatus().name()
            ))
            .toList();

        return productsDTO;
    }
}
