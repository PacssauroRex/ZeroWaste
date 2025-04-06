package com.zerowaste.services.promotions;

import com.zerowaste.models.promotion.Promotion;
import com.zerowaste.repositories.PromotionsRepository;
import com.zerowaste.services.promotions.exceptions.PromotionNotFoundException;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class GetPromotionProductService {

    private PromotionsRepository promotionsRepository;

    public GetPromotionProductService(PromotionsRepository promotionsRepository) {
        this.promotionsRepository = promotionsRepository;
    }

    public List<Promotion> execute(String productName) throws PromotionNotFoundException {
        List<Promotion> promotions = promotionsRepository.findByProducts_Name(productName);
        if (promotions.isEmpty() || promotions.stream().allMatch(promo -> promo.getDeletedAt() != null)) {
            throw new PromotionNotFoundException("Nenhuma promoção encontrada para o produto indicado.");
        }
        return promotions;
    }
}
