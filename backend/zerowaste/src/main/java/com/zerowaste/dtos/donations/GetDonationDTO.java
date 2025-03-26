package com.zerowaste.dtos.donations;

import java.time.LocalDate;

public class GetDonationDTO {
    private Long id;
    private String name;
    private LocalDate date;

    public GetDonationDTO () {}

    public GetDonationDTO(Long id, String name, LocalDate date) {
        this.id = id;
        this.name = name;
        this.date = date;
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

    public LocalDate getDate() {
        return this.date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
