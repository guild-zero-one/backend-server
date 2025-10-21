package com.zeroone.simlady.core.adapters.dtos.pedido;

import java.util.List;
import java.util.UUID;

public record PedidoCreateRequestDto(
        UUID idUsuario,
        List<PedidoItemRequestDto> itens
) {}
