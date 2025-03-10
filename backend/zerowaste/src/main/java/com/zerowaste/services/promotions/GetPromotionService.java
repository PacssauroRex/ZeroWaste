package com.zerowaste.services.promotions;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zerowaste.dtos.promotions.PromotionDTO;
import com.zerowaste.models.promotion.Promotion;
import com.zerowaste.repositories.PromotionsRepository;

@Service
public class GetPromotionService {
    @Autowired
    private PromotionsRepository promotionsRepository;

    public List<PromotionDTO> execute() {
        List<Promotion> promos = promotionsRepository.findAllNotDeleted();
        List<PromotionDTO> promotionDTOs = new ArrayList<>();
        for (var p : promos) {
            promotionDTOs.add(new PromotionDTO(p.getName(), p.getPercentage(), p.getStartsAt(), p.getEndsAt()));
        }

        return promotionDTOs;
    }
}
