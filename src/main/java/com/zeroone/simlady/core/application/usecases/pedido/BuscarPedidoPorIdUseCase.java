package com.zeroone.simlady.core.application.usecases.pedido;

import com.zeroone.simlady.core.application.ports.PedidoRepositoryPort;
import com.zeroone.simlady.core.domain.pedido.Pedido;
import com.zeroone.simlady.infrastructure.exception.ResourceNotFoundException;

import java.util.UUID;

public class BuscarPedidoPorIdUseCase {
    private final PedidoRepositoryPort repository;

    public BuscarPedidoPorIdUseCase(PedidoRepositoryPort repository) {
        this.repository = repository;
    }

    public Pedido executar(UUID id) {
        return repository.buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado com ID: " + id));
    }
}
