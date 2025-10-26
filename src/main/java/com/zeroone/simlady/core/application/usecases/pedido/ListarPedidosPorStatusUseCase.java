package com.zeroone.simlady.core.application.usecases.pedido;

import com.zeroone.simlady.core.application.ports.PedidoRepositoryPort;
import com.zeroone.simlady.core.domain.pedido.Pedido;
import org.springframework.data.domain.Page;

public class ListarPedidosPorStatusUseCase {
    private final PedidoRepositoryPort repository;

    public ListarPedidosPorStatusUseCase(PedidoRepositoryPort repository) {
        this.repository = repository;
    }

    public Page<Pedido> executar(String status, int pagina, int tamanho) {
        return repository.listarPorStatus(status, pagina, tamanho);
    }
}
