package com.zerowaste.dtos;

import com.zerowaste.models.user.UserRole;
import com.zerowaste.utils.validation.ValidEnum.ValidEnum;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterUserDTO(
    @NotBlank(message = "O campo \"name\" é obrigatório")
    String name,
    
    @NotBlank(message = "O campo \"email\" é obrigatório")
    @Email(message = "O campo \"email\" deve estar em um formato válido")
    String email,
    
    @NotBlank(message = "O campo \"password\" é obrigatório")
    @Size(min = 8, message = "O campo \"password\" deve ter no mínimo 8 caracteres")
    String password,
    
    @NotBlank(message = "O campo \"role\" é obrigatório")
    @ValidEnum(targetClassType = UserRole.class, message = "O campo \"role\" deve ser um dos valores: 'ALUNO', 'PROFESSOR'")
    String role
) {}
