package com.zerowaste.services.promotions;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zerowaste.dtos.promotions.AddPromotionDTO;
import com.zerowaste.models.promotion.Promotion;
import com.zerowaste.repositories.PromotionsRepository;

@Service
public class CreatePromotionService {
    @Autowired
    private PromotionsRepository promotionsRepository;

    public void execute(AddPromotionDTO dto) {
        Promotion p = new Promotion();
        p.setName(dto.name());
        p.setPercentage(dto.percentage());
        p.setStartsAt(dto.startsAt());
        p.setEndsAt(dto.endsAt());
        p.setCreatedAt(LocalDate.now());

        promotionsRepository.save(p);
    }
}
