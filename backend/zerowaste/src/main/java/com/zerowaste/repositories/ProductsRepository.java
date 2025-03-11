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
    
    Optional<Product> findById(Long id);
    List<Product> findAllByNameLike(String name);
}
