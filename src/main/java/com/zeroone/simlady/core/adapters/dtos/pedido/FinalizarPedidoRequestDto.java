package com.zeroone.simlady.core.adapters.dtos.pedido;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDate;

public record FinalizarPedidoRequestDto(
    @PositiveOrZero(message = "Desconto deve ser maior ou igual a zero")
    Double desconto,
    
    @NotNull(message = "Data da venda é obrigatória")
    LocalDate dataVenda
) {}
