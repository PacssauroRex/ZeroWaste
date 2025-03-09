package com.zerowaste.services.promotions;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zerowaste.models.promotion.Promotion;
import com.zerowaste.repositories.PromotionsRepository;
import com.zerowaste.services.promotions.exceptions.PromotionNotFoundException;

@Service
public class DeletePromotionService {

    @Autowired
    private PromotionsRepository promotionsRepository;

    public void execute(Long id) throws PromotionNotFoundException {

        Promotion p = promotionsRepository.findById(id).get();

        if (p == null || p.getDeletedAt() != null)
            throw new PromotionNotFoundException("Promoção não encontrada!");

        p.setDeletedAt(LocalDate.now());

        promotionsRepository.save(p);

    }

}
