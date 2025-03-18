package com.zerowaste.services.promotions;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.zerowaste.dtos.promotions.AddPromotionDTO;
import com.zerowaste.models.promotion.Promotion;
import com.zerowaste.repositories.PromotionsRepository;
import com.zerowaste.services.promotions.exceptions.InvalidDatePeriodException;

@Service
public class CreatePromotionService {

    private final PromotionsRepository promotionsRepository;

    public CreatePromotionService(PromotionsRepository promotionsRepository) {
        this.promotionsRepository = promotionsRepository;
    }

    public void execute(AddPromotionDTO dto) throws InvalidDatePeriodException {

        if (dto.startsAt().isAfter(dto.endsAt()))
            throw new InvalidDatePeriodException("The start date must be before the end date.");

        Promotion p = new Promotion();

        p.setName(dto.name());
        p.setPercentage(dto.percentage());
        p.setStartsAt(dto.startsAt());
        p.setEndsAt(dto.endsAt());
        p.setCreatedAt(LocalDate.now());

        promotionsRepository.save(p);
    }
}
