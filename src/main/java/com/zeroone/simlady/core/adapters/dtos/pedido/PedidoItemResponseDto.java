package com.zeroone.simlady.core.adapters.dtos.pedido;

import java.util.UUID;

public record PedidoItemResponseDto(
        UUID id,
        UUID idProduto,
        Integer quantidade,
        String precoUnitario,
        String subtotal
) {}
