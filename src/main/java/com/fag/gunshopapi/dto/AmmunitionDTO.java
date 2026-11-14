package com.fag.gunshopapi.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record AmmunitionDTO(
        Integer id,
        @NotBlank(message = "Marca é obrigatória") String brand,
        @NotBlank(message = "Calibre é obrigatório") String caliber,
        @NotNull(message = "Quantidade por caixa é obrigatória") @Min(value = 1, message = "Mínimo 1 por caixa") Integer quantityPerBox,
        @NotNull(message = "Preço é obrigatório") @Positive(message = "Preço deve ser maior que zero") BigDecimal price,
        @NotNull(message = "Estoque é obrigatório") @Min(value = 0, message = "Estoque não pode ser negativo") Integer stock
) {}
