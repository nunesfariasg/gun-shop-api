package com.fag.gunshopapi.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record CustomerDTO(
        Integer id,
        @NotBlank(message = "Nome é obrigatório") String name,
        @NotBlank(message = "CPF é obrigatório") @Size(min = 11, max = 11, message = "CPF deve ter 11 dígitos") String cpf,
        @NotNull(message = "Data de nascimento é obrigatória") @Past(message = "Data deve estar no passado") LocalDate birthDate,
        @NotBlank(message = "Número do CR é obrigatório") String crNumber
) {}
