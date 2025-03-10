package com.zerowaste.services.promotions;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zerowaste.models.promotion.Promotion;
import com.zerowaste.repositories.PromotionsRepository;
import com.zerowaste.services.promotions.exceptions.PromotionNotFoundException;

@Service
public class GetPromotionProductService {
    @Autowired
    private PromotionsRepository promotionsRepository;

    public List<Promotion> execute(Long productsIds) throws PromotionNotFoundException {
        List<Promotion> promotions = promotionsRepository.findByProducts_Id(productsIds);
        if (promotions.isEmpty() || promotions.stream().allMatch(promo -> promo.getDeletedAt() != null)) {
            throw new PromotionNotFoundException("Nenhuma promoção encontrada para o produto indicado.");
        }
        return promotions;
    }
}
