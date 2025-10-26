package com.zeroone.simlady.core.adapters.dtos.pedido;

import java.util.List;
import java.util.UUID;

public record PedidoUpdateRequestDto(
        UUID idVenda,
        UUID idUsuario,
        List<PedidoItemRequestDto> itens
) {}
