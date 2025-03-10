package com.zerowaste.services.promotions;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zerowaste.models.promotion.Promotion;
import com.zerowaste.repositories.PromotionsRepository;
import com.zerowaste.services.promotions.exceptions.PromotionNotFoundException;

@Service
public class GetPromotionsIdService {
    @Autowired
    private PromotionsRepository promotionsRepository;

    public Promotion execute(Long id) throws PromotionNotFoundException {
        Optional<Promotion> p = promotionsRepository.findById(id);
        if(!p.isPresent() || p.get().getDeletedAt() != null) 
            throw new PromotionNotFoundException("Promoção não encontrada!");
        
        return p.get();
    }
}
