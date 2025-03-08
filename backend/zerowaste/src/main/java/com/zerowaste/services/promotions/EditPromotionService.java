package com.zerowaste.services.promotions;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zerowaste.dtos.promotions.EditPromotionDTO;
import com.zerowaste.models.promotion.Promotion;
import com.zerowaste.repositories.PromotionsRepository;
import com.zerowaste.services.promotions.exceptions.PromotionNotFoundException;

@Service
public class EditPromotionService {

    @Autowired
    private PromotionsRepository promotionsRepository;

    public void execute(Long id, EditPromotionDTO dto) throws PromotionNotFoundException {

        Promotion p = getPromotionById(id);

        p.setName(dto.name());
        p.setPercentage(dto.percentage());
        p.setStartsAt(dto.startsAt());
        p.setEndsAt(dto.endsAt());
        p.setUpdatedAt(LocalDate.now());

        promotionsRepository.save(p);
    }

    public Promotion getPromotionById(Long id) throws PromotionNotFoundException {

        Optional<Promotion> p = promotionsRepository.findById(id);

        if (!p.isPresent() || p.get().getDeletedAt() != null)
            throw new PromotionNotFoundException("Promoção não encontrada!");

        return p.get();

    }

}