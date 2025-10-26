package com.zeroone.simlady.core.application.usecases.pedido;

import com.zeroone.simlady.core.application.ports.PedidoRepositoryPort;
import com.zeroone.simlady.core.domain.pedido.Pedido;
import org.springframework.data.domain.Page;

public class ListarPedidosUseCase {
    private final PedidoRepositoryPort repository;

    public ListarPedidosUseCase(PedidoRepositoryPort repository) {
        this.repository = repository;
    }

    public Page<Pedido> executar(int pagina, int tamanho) {
        return repository.listarTodos(pagina, tamanho);
    }
}
