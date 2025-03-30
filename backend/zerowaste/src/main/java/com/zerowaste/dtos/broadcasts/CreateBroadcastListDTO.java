package com.zerowaste.dtos.broadcasts;

import java.util.List;

import com.zerowaste.models.broadcast.BroadcastListSendType;
import com.zerowaste.utils.validation.ValidEnum.ValidEnum;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record CreateBroadcastListDTO(
        @NotBlank(message = "O campo \"name\" é obrigatório")
        @Size(max = 100, message = "O campo \"name\" deve ter entre 3 e 100 caracteres")
        String name,

        @NotBlank(message = "O campo \"description\" é obrigatório")
        @Size(max = 255, message = "O campo \"description\" deve ter entre 3 e 255 caracteres")
        String description,

        @NotBlank(message = "O campo \"sendType\" é obrigatório")
        @ValidEnum(targetClassType = BroadcastListSendType.class, message = "O campo \"type\" deve ser um dos valores: 'MANUAL', 'INTERVAL'")
        String sendType,

        @Valid
        @NotEmpty(message = "O campo \"emails\" não pode ser vazio")
        @Size(min = 1, message = "O campo \"emails\" deve ter no mínimo 1 email")
        List<
            @NotBlank(message = "O campo \"email\" é obrigatório")
            @Size(max = 320, message = "O campo \"email\" deve ter no máximo 320 caracteres")
            @Email(message = "O campo \"email\" deve estar em um formato válido")
            String
        > emails,

        @Valid
        @NotEmpty(message = "O campo \"productsIds\" não pode ser vazio")
        @Size(min = 1, message = "O campo \"productsIds\" deve ter no mínimo 1 id de produto")
        List<Long> productsIds
) {}
