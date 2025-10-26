package com.zeroone.simlady.core.application.usecases.pedido;

import com.zeroone.simlady.core.application.ports.PedidoRepositoryPort;
import com.zeroone.simlady.core.domain.pedido.Pedido;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AtualizarPedidoUseCase {
    private final PedidoRepositoryPort repository;
    private final ValidarEstoquePedidoUseCase validarEstoquePedidoUseCase;

    public Pedido executar(Pedido pedido) {
        // Validar estoque antes de atualizar o pedido
        validarEstoquePedidoUseCase.executar(pedido.getItens());
        
        return repository.atualizarPedido(pedido);
    }
}
