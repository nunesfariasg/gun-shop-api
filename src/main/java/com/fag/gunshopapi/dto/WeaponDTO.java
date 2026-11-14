package com.fag.gunshopapi.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record WeaponDTO(
        Integer id,
        @NotBlank(message = "Modelo é obrigatório") String model,
        @NotBlank(message = "Marca é obrigatória") String brand,
        @NotBlank(message = "Calibre é obrigatório") String caliber,
        @NotBlank(message = "Tipo é obrigatório") String type,
        @NotNull(message = "Preço é obrigatório") @Positive(message = "Preço deve ser maior que zero") BigDecimal price,
        @NotNull(message = "Estoque é obrigatório") @Min(value = 0, message = "Estoque não pode ser negativo") Integer stock
) {}
