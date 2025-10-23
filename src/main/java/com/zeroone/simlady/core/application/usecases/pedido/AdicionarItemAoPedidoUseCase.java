package com.zeroone.simlady.core.application.usecases.pedido;

import com.zeroone.simlady.core.application.ports.PedidoRepositoryPort;
import com.zeroone.simlady.core.domain.pedido.Pedido;
import com.zeroone.simlady.core.domain.pedido.PedidoItem;
import com.zeroone.simlady.infrastructure.exception.ResourceNotFoundException;

import java.util.UUID;

public class AdicionarItemAoPedidoUseCase {
    private final PedidoRepositoryPort repository;

    public AdicionarItemAoPedidoUseCase(PedidoRepositoryPort repository) {
        this.repository = repository;
    }

    public Pedido executar(UUID pedidoId, UUID idProduto, Integer quantidade, String precoUnitario) {
        Pedido pedido = repository.buscarPorId(pedidoId)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado com ID: " + pedidoId));

        PedidoItem novoItem = PedidoItem.newPedidoItem(idProduto, quantidade, precoUnitario);
        pedido.adicionarItem(novoItem);

        return repository.atualizarPedido(pedido);
    }
}
