package com.fag.gunshopapi.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record SaleResponseDTO(
        Integer id,
        Integer customerId,
        String customerName,
        LocalDateTime saleDate,
        BigDecimal totalAmount,
        String status,
        List<SaleItemResponseDTO> items
) {}
