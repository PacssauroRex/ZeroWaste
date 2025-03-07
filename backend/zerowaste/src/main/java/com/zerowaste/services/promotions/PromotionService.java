package com.zerowaste.services.promotions;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zerowaste.dtos.promotions.AddPromotionDTO;
import com.zerowaste.dtos.promotions.PromotionDTO;
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
        p.setCreatedAt(LocalDate.now());

        promotionsRepository.save(p);
    }

    public List<PromotionDTO> getAllPromotions() {
        List<Promotion> promos = promotionsRepository.findAllNotDeleted();
        List<PromotionDTO> promotionDTOs = new ArrayList<>();
        for (var p : promos) {
            promotionDTOs.add(new PromotionDTO(p.getName(), p.getPercentage(), p.getStartsAt(), p.getEndsAt()));
        }

        return promotionDTOs;
    }
}
