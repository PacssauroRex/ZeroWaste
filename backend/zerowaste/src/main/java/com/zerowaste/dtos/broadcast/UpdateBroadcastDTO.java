package com.zerowaste.dtos.broadcast;

import java.time.LocalDate;
import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateBroadcastDTO(
    @NotEmpty(message = "O campo \"email\" é obrigatório")
    @Email(message = "O campo \"email\" deve ser um email válido")
    String email,

    @NotEmpty(message = "O campo \"name\" é obrigatório")
    @Size(min = 3, max = 100, message = "O campo \"name\" deve ter entre 3 e 100 caracteres")
    String name,

    @NotNull(message = "O campo \"updatedAt\" é obrigatório")
    @Future(message = "O campo \"updatedAt\" deve ser uma data futura")
    LocalDate updatedAt,

    Set<Long> broadcastEmailIds  // IDs dos emails associados
){}

