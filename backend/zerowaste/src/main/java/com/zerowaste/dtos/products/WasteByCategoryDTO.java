package com.zerowaste.dtos.products;

public record WasteByCategoryDTO(
    String category,
    long quantity, 
    double cost
) {}
