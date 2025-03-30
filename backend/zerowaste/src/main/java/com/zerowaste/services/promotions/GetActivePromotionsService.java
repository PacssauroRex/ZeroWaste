package com.zerowaste.services.promotions;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.zerowaste.models.promotion.Promotion;
import com.zerowaste.repositories.PromotionsRepository;

@Service
public class GetActivePromotionsService {
    
    private final PromotionsRepository promotionsRepository;

    public GetActivePromotionsService (PromotionsRepository promotionsRepository) {
        this.promotionsRepository = promotionsRepository;
    }

    public List<Promotion> execute() {
        return promotionsRepository.findAllActive(LocalDate.now().toString());
    }
}
