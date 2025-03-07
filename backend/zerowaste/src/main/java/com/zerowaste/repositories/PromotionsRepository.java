package com.zerowaste.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.stereotype.Repository;

import com.zerowaste.dtos.promotions.PromotionDTO;
import com.zerowaste.models.promotion.Promotion;

@Repository
public interface PromotionsRepository extends JpaRepository<Promotion, Long> {
    @NativeQuery("SELECT name, percentage, starts_at, ends_at " +
                "FROM promotions WHERE deleted_at IS NULL")
    List<PromotionDTO> findAllNotDeleted();
}
