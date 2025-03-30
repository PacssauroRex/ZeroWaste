package com.zerowaste.dtos.broadcast;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.Set;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;

public record CreateBroadcastDTO(
    @NotEmpty(message = "O campo \"email\" é obrigatório")
    @Email(message = "O campo \"email\" deve ser um email válido")
    String email,

    @NotEmpty(message = "O campo \"name\" é obrigatório")
    @Size(min = 3, max = 100, message = "O campo \"name\" deve ter entre 3 e 100 caracteres")
    String name,

    @NotNull(message = "O campo \"createdAt\" é obrigatório")
    @Future(message = "O campo \"createdAt\" deve ser uma data futura")
    LocalDate createdAt,

    Set<Long> broadcastEmailIds  // IDs dos emails associados
){}

