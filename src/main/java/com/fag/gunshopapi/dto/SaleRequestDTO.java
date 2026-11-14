package com.fag.gunshopapi.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record SaleRequestDTO(
        @NotNull(message = "ID do cliente é obrigatório") Integer customerId,
        @NotEmpty(message = "A venda deve conter ao menos um item") @Valid List<SaleItemRequestDTO> items
) {}
