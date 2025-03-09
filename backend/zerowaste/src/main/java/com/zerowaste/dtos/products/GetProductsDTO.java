package com.zerowaste.dtos.products;

import jakarta.validation.constraints.Min;

public record GetProductsDTO(
    @Min(0)
    Integer daysToExpire
) {}