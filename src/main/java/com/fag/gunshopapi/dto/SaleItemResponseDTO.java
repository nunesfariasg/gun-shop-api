package com.fag.gunshopapi.dto;

import java.math.BigDecimal;

public record SaleItemResponseDTO(
        Integer id,
        String productType,
        String productDescription,
        String caliber,
        Integer quantity,
        BigDecimal unitPrice
) {}
