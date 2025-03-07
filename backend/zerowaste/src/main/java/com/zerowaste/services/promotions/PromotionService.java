package com.zerowaste.services.promotions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zerowaste.dtos.promotions.AddPromotionDTO;
import com.zerowaste.models.promotion.Promotion;
import com.zerowaste.repositories.PromotionsRepository;

@Service
public class PromotionService {
    @Autowired
    private PromotionsRepository promotionsRepository;

    public void createPromotion(AddPromotionDTO dto) {
        Promotion p = new Promotion();
        p.setName(dto.name());
        p.setPercentage(dto.percentage());
        p.setStartsAt(dto.startsAt());
        p.setEndsAt(dto.endsAt());

        promotionsRepository.save(p);
    }
}
