package com.zerowaste.dtos.promotions;

import java.time.LocalDate;

public record PromotionDTO (
    String name,
    Double percentage,
    LocalDate startsAt,
    LocalDate endsAt
) {}
