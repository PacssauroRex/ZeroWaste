package com.zerowaste.services.promotions;

import com.zerowaste.models.promotion.Promotion;
import com.zerowaste.repositories.PromotionsRepository;
import com.zerowaste.services.promotions.exceptions.PromotionNotFoundException;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class GetPromotionsIdService {

    private final PromotionsRepository promotionsRepository;

    public GetPromotionsIdService(PromotionsRepository promotionsRepository) {
        this.promotionsRepository = promotionsRepository;
    }

    public Promotion execute(Long id) throws PromotionNotFoundException {
        Optional<Promotion> p = promotionsRepository.findById(id);
        if(!p.isPresent() || p.get().getDeletedAt() != null) 
            throw new PromotionNotFoundException("Promoção não encontrada!");
        
        return p.get();
    }
}
