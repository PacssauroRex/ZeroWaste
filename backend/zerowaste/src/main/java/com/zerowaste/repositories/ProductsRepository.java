package com.zerowaste.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zerowaste.models.product.Product;

@Repository
public interface ProductsRepository extends JpaRepository<Product, Long> {
}