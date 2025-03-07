package com.zerowaste.dtos.products;

import java.time.LocalDate;

import com.zerowaste.models.product.ProductCategory;
import com.zerowaste.utils.validation.ValidEnum.ValidEnum;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record EditProductDTO(
    @NotBlank(message = "O campo \"nome\" é obrigatório")
    String name,

    @NotBlank(message = "O campo \"descrição\" é obrigatório")
    String description,

    @NotBlank(message = "O campo \"marca\" é obrigatório")
    String brand, 

    @NotBlank(message = "O campo \"categoria\" é obrigatório")
    @ValidEnum(targetClassType = ProductCategory.class, message = "O campo \"categoria\" deve conter uma categoria válida")
    String category,

    @DecimalMin("0")
    Double unitPrice,

    @DecimalMin("0")
    Double promotionPrice,

    @FutureOrPresent
    LocalDate expiresAt,

    @Min(0)
    Integer stock
) {} 
