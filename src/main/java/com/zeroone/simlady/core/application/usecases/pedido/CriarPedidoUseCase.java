package com.zeroone.simlady.core.application.usecases.pedido;

import com.zeroone.simlady.core.application.ports.PedidoRepositoryPort;
import com.zeroone.simlady.core.application.ports.ProdutoRepositoryPort;
import com.zeroone.simlady.core.domain.pedido.Pedido;
import com.zeroone.simlady.core.domain.pedido.PedidoItem;
import com.zeroone.simlady.infrastructure.exception.ResourceNotFoundException;

import java.util.List;
import java.util.UUID;

public class CriarPedidoUseCase {
    private final PedidoRepositoryPort repository;
    private final ProdutoRepositoryPort produtoRepositoryPort;

    public CriarPedidoUseCase(PedidoRepositoryPort repository, ProdutoRepositoryPort produtoRepositoryPort) {
        this.repository = repository;
        this.produtoRepositoryPort = produtoRepositoryPort;
    }

    public Pedido executar(UUID idVenda, UUID idUsuario, List<PedidoItem> itens) {
        Pedido novoPedido = Pedido.newPedido(idVenda, idUsuario);
        
        if (itens != null) {
            for (PedidoItem item : itens) {
                UUID idProduto = item.getIdProduto();
                
                if (!produtoRepositoryPort.buscarPorId(idProduto).isPresent()) {
                    throw new ResourceNotFoundException("Produto não encontrado com ID: " + idProduto);
                }
                
                novoPedido.adicionarItem(item);
            }
        }
        
        return repository.salvarPedido(novoPedido);
    }
}
