package com.zerowaste.dtos.donations;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record CreateDonationDTO (
    @NotEmpty(message = "O campo \"name\" é obrigatorio")
    @Size(max = 100, message = "O campo \"name\" deve ter no máximo 100 caracteres")
    String name,

    List<Long> productsId,

    @FutureOrPresent
    LocalDate date
){}
