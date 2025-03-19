package com.zerowaste.services.promotions;

import com.zerowaste.models.promotion.Promotion;
import com.zerowaste.repositories.PromotionsRepository;
import com.zerowaste.services.promotions.exceptions.PromotionNotFoundException;
import java.time.LocalDate;
import org.springframework.stereotype.Service;

@Service
public class DeletePromotionService {

    private PromotionsRepository promotionsRepository;

    public DeletePromotionService(PromotionsRepository promotionsRepository) {
        this.promotionsRepository = promotionsRepository;
    }

    public void execute(Long id) throws PromotionNotFoundException {

        Promotion p = promotionsRepository.findById(id).get();

        if (p == null || p.getDeletedAt() != null)
            throw new PromotionNotFoundException("Promoção não encontrada!");

        p.setDeletedAt(LocalDate.now());

        promotionsRepository.save(p);

    }

}
