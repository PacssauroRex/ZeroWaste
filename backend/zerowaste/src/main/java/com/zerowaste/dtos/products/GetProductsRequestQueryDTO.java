package com.zerowaste.dtos.products;

import jakarta.validation.constraints.Min;

public record GetProductsRequestQueryDTO(
    @Min(0)
    Integer daysToExpire
) {}