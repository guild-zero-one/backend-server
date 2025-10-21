package com.zeroone.simlady.core.application.usecases.pedido;

import com.zeroone.simlady.core.application.ports.PedidoRepositoryPort;

import java.util.UUID;

public class DeletarPedidoPorIdUseCase {
    private final PedidoRepositoryPort repository;

    public DeletarPedidoPorIdUseCase(PedidoRepositoryPort repository) {
        this.repository = repository;
    }

    public void executar(UUID id) {
        repository.deletarPorId(id);
    }
}
