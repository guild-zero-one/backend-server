package com.zeroone.simlady.core.adapters.dtos.pedido;

import java.math.BigDecimal;

public record PedidoItemRequestDto(
        Integer idProduto,
        Integer quantidade,
        BigDecimal precoUnitario
) {}
