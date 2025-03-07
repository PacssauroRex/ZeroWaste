package com.zerowaste.dtos.promotions;

import java.time.LocalDate;

public class PromotionDTO {
    private String name;
    private Double percentage;
    private LocalDate startsAt;
    private LocalDate endsAt;

    public PromotionDTO(String name, Double percentage, LocalDate startsAt, LocalDate endsAt) {
        this.name = name;
        this.percentage = percentage;
        this.startsAt = startsAt;
        this.endsAt = endsAt;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPercentage() {
        return this.percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

    public LocalDate getStartsAt() {
        return this.startsAt;
    }

    public void setStartsAt(LocalDate startsAt) {
        this.startsAt = startsAt;
    }

    public LocalDate getEndsAt() {
        return this.endsAt;
    }

    public void setEndsAt(LocalDate endsAt) {
        this.endsAt = endsAt;
    }
}