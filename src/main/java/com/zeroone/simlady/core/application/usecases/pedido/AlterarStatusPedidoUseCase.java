package com.zeroone.simlady.core.application.usecases.pedido;

import com.zeroone.simlady.core.application.ports.PedidoRepositoryPort;
import com.zeroone.simlady.core.domain.pedido.Pedido;
import com.zeroone.simlady.core.domain.pedido.StatusPedido;
import com.zeroone.simlady.infrastructure.exception.ResourceNotFoundException;

import java.util.UUID;

public class AlterarStatusPedidoUseCase {
    private final PedidoRepositoryPort repository;

    public AlterarStatusPedidoUseCase(PedidoRepositoryPort repository) {
        this.repository = repository;
    }

    public Pedido executar(UUID pedidoId, StatusPedido novoStatus) {
        Pedido pedido = repository.buscarPorId(pedidoId)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado com ID: " + pedidoId));

        pedido.alterarStatus(novoStatus);

        return repository.atualizarPedido(pedido);
    }
}
