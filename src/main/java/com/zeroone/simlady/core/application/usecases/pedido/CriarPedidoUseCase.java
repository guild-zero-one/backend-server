package com.zeroone.simlady.core.application.usecases.pedido;

import com.zeroone.simlady.core.application.ports.PedidoRepositoryPort;
import com.zeroone.simlady.core.application.ports.ProdutoRepository;
import com.zeroone.simlady.core.domain.pedido.Pedido;
import com.zeroone.simlady.core.domain.pedido.PedidoItem;
import com.zeroone.simlady.exception.ResourceNotFoundException;

import java.util.List;
import java.util.UUID;

public class CriarPedidoUseCase {
    private final PedidoRepositoryPort repository;
    private final ProdutoRepository produtoRepository;

    public CriarPedidoUseCase(PedidoRepositoryPort repository, ProdutoRepository produtoRepository) {
        this.repository = repository;
        this.produtoRepository = produtoRepository;
    }

    public Pedido executar(UUID idVenda, UUID idUsuario, List<PedidoItem> itens) {
        Pedido novoPedido = Pedido.newPedido(idVenda, idUsuario);
        
        if (itens != null) {
            for (PedidoItem item : itens) {
                UUID idProduto = item.getIdProduto();
                
                if (!produtoRepository.buscarPorId(idProduto).isPresent()) {
                    throw new ResourceNotFoundException("Produto não encontrado com ID: " + idProduto);
                }
                
                novoPedido.adicionarItem(item);
            }
        }
        
        return repository.salvarPedido(novoPedido);
    }
}
