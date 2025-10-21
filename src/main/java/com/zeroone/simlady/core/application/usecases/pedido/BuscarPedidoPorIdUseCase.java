package com.zeroone.simlady.core.application.usecases.pedido;

import com.zeroone.simlady.core.application.ports.PedidoRepositoryPort;
import com.zeroone.simlady.core.domain.pedido.Pedido;

import java.util.UUID;

public class BuscarPedidoPorIdUseCase {
    private final PedidoRepositoryPort repository;

    public BuscarPedidoPorIdUseCase(PedidoRepositoryPort repository) {
        this.repository = repository;
    }

    public Pedido executar(UUID id) {
        return repository.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado com ID: " + id));
    }
}
