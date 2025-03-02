package com.zerowaste.zerowaste.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AuthenticateUserDTO(
    @NotBlank(message = "O campo \"email\" é obrigatório")
    @Email(message = "O campo \"email\" deve estar em um formato válido")
    String email,
    
    @NotBlank(message = "O campo \"password\" é obrigatório")
    @Size(min = 8, message = "O campo \"password\" deve ter no mínimo 8 caracteres")
    String password
) {}
