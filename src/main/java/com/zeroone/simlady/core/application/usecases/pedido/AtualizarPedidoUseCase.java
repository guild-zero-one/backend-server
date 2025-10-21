package com.zeroone.simlady.core.application.usecases.pedido;

import com.zeroone.simlady.core.application.ports.PedidoRepositoryPort;
import com.zeroone.simlady.core.domain.pedido.Pedido;

public class AtualizarPedidoUseCase {
    private final PedidoRepositoryPort repository;

    public AtualizarPedidoUseCase(PedidoRepositoryPort repository) {
        this.repository = repository;
    }

    public Pedido executar(Pedido pedido) {
        return repository.atualizarPedido(pedido);
    }
}
