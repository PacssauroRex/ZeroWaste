package com.zerowaste.repositories;

import com.zerowaste.models.promotion.Promotion;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PromotionsRepository extends JpaRepository<Promotion, Long> {
    @NativeQuery("SELECT * FROM promotions WHERE deleted_at IS NULL")
    List<Promotion> findAllNotDeleted();

    @NativeQuery("SELECT * FROM promotions WHERE deleted_at IS NULL AND " +
                "starts_at <= CAST(:currentDate AS DATE) AND ends_at >= CAST(:currentDate AS DATE)")
    List<Promotion> findAllActive(@Param("currentDate") String currentDate);

    Optional<Promotion> findById(Long id);
    List<Promotion> findByPercentageLessThanEqual(int percentage);
    List<Promotion> findByProducts_Name(String productName);
}
