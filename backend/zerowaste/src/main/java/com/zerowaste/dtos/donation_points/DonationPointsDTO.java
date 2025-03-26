package com.zerowaste.dtos.donation_points;

import java.time.LocalTime;

public record DonationPointsDTO (
    Long id,
    String name,
    LocalTime openingTime,
    LocalTime closingTime,
    String email,
    String street,
    int number,
    String city
) {}
