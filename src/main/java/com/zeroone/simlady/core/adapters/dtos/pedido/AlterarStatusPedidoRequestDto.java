package com.zeroone.simlady.core.adapters.dtos.pedido;

import com.zeroone.simlady.core.domain.pedido.StatusPedido;

public record AlterarStatusPedidoRequestDto(
        StatusPedido status
) {}
