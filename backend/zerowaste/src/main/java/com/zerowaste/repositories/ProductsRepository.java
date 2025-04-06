package com.zerowaste.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.zerowaste.models.product.Product;

@Repository
public interface ProductsRepository extends JpaRepository<Product, Long> {
    @NativeQuery(
        "SELECT * FROM products WHERE deleted_at IS NULL " +
        "AND (:daysToExpire IS NULL OR expires_at = CURRENT_DATE + INTERVAL '1 day' * :daysToExpire)"
    )
    List <Product> findAllNotDeleted(@Param("daysToExpire") Integer daysToExpire);
    
    @NativeQuery (
        "SELECT COUNT(*) FROM products WHERE deleted_at IS NULL " + 
        "AND expires_at <= CAST(:targetDate AS DATE)"
    )
    Long countExpiringProducts(@Param("targetDate") String targetDate);

    @NativeQuery (
        "SELECT COUNT(*) FROM products WHERE deleted_at IS NULL " + 
        "AND expires_at > CAST(:startDate AS DATE) " + 
        "AND expires_at < CAST(:endDate AS DATE)"
    )
    Integer countExpiredProductsBetween(@Param("startDate") String startDate, @Param("endDate") String endDate);

    @NativeQuery (
        "SELECT SUM(unit_price) FROM products WHERE deleted_at IS NULL " + 
        "AND expires_at > CAST(:startDate AS DATE) " + 
        "AND expires_at < CAST(:endDate AS DATE)"
    )
    Double sumUnitPriceExpiredProductsBetween(@Param("startDate") String startDate, @Param("endDate") String endDate);

    @NativeQuery(
        "SELECT category, COUNT (*) as counting, SUM(unit_price) FROM products " + 
        "WHERE deleted_at IS NULL " + 
        "AND status IN ('DONATED', 'DISCARDED') " +
        "GROUP BY category " +
        "ORDER BY counting DESC" 
    )
    List<Object[]> wasteGroupByCategory();

    Optional<Product> findById(Long id);
    List<Product> findAllByNameLike(String name);
}
