package com.zerowaste.dtos.donation_points;

import java.time.LocalTime;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record CreateDonationPointDTO(

    @NotEmpty(message = "O campo \"name\" é obrigatório")
    @Size(min = 3, max = 100, message = "O campo \"name\" deve ter entre 3 e 100 caracteres")
    String name,

    @NotEmpty(message = "O campo \"openingTime\" é obrigatório")
    LocalTime openingTime,

    @NotEmpty(message = "O campo \"closingTime\" é obrigatório")
    LocalTime closingTime,

    @NotEmpty(message = "O campo \"email\" é obrigatório")
    @Email(message = "O campo \"email\" deve ser um email válido")
    String email,

    // Address information
    @NotEmpty(message = "O campo \"street\" é obrigatório")
    @Size(max = 100, message = "O campo \"street\" deve ter entre 3 e 100 caracteres")
    String street,

    @NotEmpty(message = "O campo \"number\" é obrigatório")
    @Size(min = 1, max = 10, message = "O campo \"number\" deve ter no máximo 10 caracteres")
    int number,

    @NotEmpty(message = "O campo \"city\" é obrigatório")
    @Size(min = 3, max = 100, message = "O campo \"city\" deve ter entre 3 e 100 caracteres")
    String city

) {}