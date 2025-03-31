package com.zerowaste.dtos.products;

import java.time.LocalDate;
import java.util.Set;

import com.zerowaste.models.product.ProductCategory;
import com.zerowaste.utils.validation.valid_enum.ValidEnum;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record EditProductDTO(
    @NotBlank(message = "O campo \"nome\" é obrigatório")
    @Size(max = 100, message = "O campo \"name\" deve ter entre 3 e 100 caracteres")
    String name,

    @NotBlank(message = "O campo \"descrição\" é obrigatório")
    @Size(max = 255, message = "O campo \"description\" deve ter entre 3 e 100 caracteres")
    String description,

    @NotBlank(message = "O campo \"marca\" é obrigatório")
    @Size(max = 100, message = "O campo \"brand\" deve ter entre 3 e 100 caracteres")
    String brand, 

    @NotBlank(message = "O campo \"categoria\" é obrigatório")
    @ValidEnum(targetClassType = ProductCategory.class, message = "O campo \"categoria\" deve conter uma categoria válida")
    String category,

    @NotNull(message = "O campo \"unitPrice\" é obrigatório")
    @DecimalMin("0")
    Double unitPrice,

    @DecimalMin("0")
    Double promotionPrice,

    @NotNull(message = "O campo \"stock\" é obrigatório")
    @Min(0)
    Integer stock,

    @NotNull(message = "O campo \"expiresAt\" é obrigatório")
    @Future(message = "O campo \"expiresAt\" deve ser uma data futura")
    LocalDate expiresAt,

    Set<Long> promotionsIds
) {}