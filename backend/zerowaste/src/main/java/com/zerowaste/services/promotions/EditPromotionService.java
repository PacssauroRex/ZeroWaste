package com.zerowaste.services.promotions;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zerowaste.dtos.promotions.EditPromotionDTO;
import com.zerowaste.models.product.Product;
import com.zerowaste.models.promotion.Promotion;
import com.zerowaste.repositories.ProductsRepository;
import com.zerowaste.repositories.PromotionsRepository;
import com.zerowaste.services.products.exceptions.ProductNotFoundException;
import com.zerowaste.services.promotions.exceptions.PromotionNotFoundException;

@Service
public class EditPromotionService {

    @Autowired
    private PromotionsRepository promotionsRepository;

    @Autowired
    private ProductsRepository productRepository;

    public void execute(Long id, EditPromotionDTO dto) throws PromotionNotFoundException, ProductNotFoundException {

        Promotion p = promotionsRepository.findById(id).get();

        if (p == null || p.getDeletedAt() != null)
            throw new PromotionNotFoundException("Promoção não encontrada!");

        p.setName(dto.name());
        p.setPercentage(dto.percentage());
        p.setStartsAt(dto.startsAt());
        p.setEndsAt(dto.endsAt());
        p.setUpdatedAt(LocalDate.now());

        // Encontra todos os produtos válidos
        Set<Product> products = new HashSet<Product>(productRepository.findAllById(dto.productIds()));

        // Caso nenhum produto seja válido
        if (products.isEmpty() && !dto.productIds().isEmpty())
            throw new ProductNotFoundException("Nenhum produto válido foi encontrado!");

        p.setProducts(products);

        promotionsRepository.save(p);

        // Atualiza o preço promocional dos produtos
        for (Product product : products) {
            double percentage = p.getPercentage() / 100; // valor entre 0 e 1
            double unitPrice = product.getUnitPrice();
            
            double promotionPrice = unitPrice - (unitPrice * percentage);
            product.setPromotionPrice(promotionPrice);
            productRepository.save(product);
        }

    }
}
