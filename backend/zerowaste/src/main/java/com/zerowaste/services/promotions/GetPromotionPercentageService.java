package com.zerowaste.services.promotions;

import com.zerowaste.models.promotion.Promotion;
import com.zerowaste.repositories.PromotionsRepository;
import com.zerowaste.services.promotions.exceptions.PromotionNotFoundException;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class GetPromotionPercentageService {

    private PromotionsRepository promotionsRepository;

    public GetPromotionPercentageService(PromotionsRepository promotionsRepository) {
        this.promotionsRepository = promotionsRepository;
    }

    public List<Promotion> execute(int percentage) throws PromotionNotFoundException {
        List<Promotion> promotions = promotionsRepository.findByPercentageLessThanEqual(percentage);
        if (promotions.isEmpty() || promotions.stream().allMatch(promo -> promo.getDeletedAt() != null)) {
            throw new PromotionNotFoundException("Nenhuma promoção encontrada para a porcentagem indicada.");
        }
        return promotions;
    }
}
