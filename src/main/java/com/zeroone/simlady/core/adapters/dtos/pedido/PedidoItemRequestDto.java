package com.zeroone.simlady.core.adapters.dtos.pedido;

import java.math.BigDecimal;
import java.util.UUID;

public record PedidoItemRequestDto(
        UUID idProduto,
        Integer quantidade,
        BigDecimal precoUnitario
) {}
