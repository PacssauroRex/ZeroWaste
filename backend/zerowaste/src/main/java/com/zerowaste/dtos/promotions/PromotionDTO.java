package com.zerowaste.dtos.promotions;

import java.time.LocalDate;

public class PromotionDTO {
    private Long id;
    private String name;
    private int percentage;
    private LocalDate startsAt;
    private LocalDate endsAt;

    public PromotionDTO(Long id, String name, int percentage, LocalDate startsAt, LocalDate endsAt) {
        this.id = id;
        this.name = name;
        this.percentage = percentage;
        this.startsAt = startsAt;
        this.endsAt = endsAt;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPercentage() {
        return this.percentage;
    }

    public void setPercentage(int percentage) {
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