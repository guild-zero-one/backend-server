package com.zeroone.simlady.core.adapters.dtos.pedido;

import com.zeroone.simlady.core.domain.pedido.StatusPedido;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record PedidoResponseDto(
        UUID id,
        StatusPedido status,
        UUID idVenda,
        UUID idUsuario,
        List<PedidoItemResponseDto> itens,
        String total,
        LocalDateTime criadoEm,
        LocalDateTime atualizadoEm
) {}
