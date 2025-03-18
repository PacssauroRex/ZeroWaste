package com.zerowaste.services.promotions;

import com.zerowaste.dtos.promotions.PromotionDTO;
import com.zerowaste.models.promotion.Promotion;
import com.zerowaste.repositories.PromotionsRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class GetPromotionService {

    private final PromotionsRepository promotionsRepository;

    public GetPromotionService(PromotionsRepository promotionsRepository) {
        this.promotionsRepository = promotionsRepository;
    }

    public List<PromotionDTO> execute() {
        List<Promotion> promos = promotionsRepository.findAllNotDeleted();
        List<PromotionDTO> promotionDTOs = new ArrayList<>();
        for (var p : promos) {
            promotionDTOs.add(new PromotionDTO(p.getId(), p.getName(), p.getPercentage(), p.getStartsAt(), p.getEndsAt()));
        }

        return promotionDTOs;
    }
}
