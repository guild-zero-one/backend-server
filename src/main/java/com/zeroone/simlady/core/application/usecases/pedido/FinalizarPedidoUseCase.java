package com.zeroone.simlady.core.application.usecases.pedido;

import com.zeroone.simlady.core.application.usecases.produto.BuscarProdutoPorIdUseCase;
import com.zeroone.simlady.core.application.usecases.produto.AtualizarProdutoUseCase;
import com.zeroone.simlady.core.application.usecases.venda.CriarVendaUseCase;
import com.zeroone.simlady.core.domain.pedido.Pedido;
import com.zeroone.simlady.core.domain.pedido.PedidoItem;
import com.zeroone.simlady.core.domain.pedido.StatusPedido;
import com.zeroone.simlady.core.domain.produto.Produto;
import com.zeroone.simlady.core.domain.venda.Venda;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class FinalizarPedidoUseCase {

    private final BuscarPedidoPorIdUseCase buscarPedidoPorIdUseCase;
    private final BuscarProdutoPorIdUseCase buscarProdutoPorIdUseCase;
    private final AtualizarProdutoUseCase atualizarProdutoUseCase;
    private final AlterarStatusPedidoUseCase alterarStatusPedidoUseCase;
    private final CriarVendaUseCase criarVendaUseCase;
    private final ValidarEstoquePedidoUseCase validarEstoquePedidoUseCase;

    @Transactional
    public Venda executar(UUID pedidoId, Double desconto, LocalDate dataVenda) {
        // 1. Buscar o pedido
        Pedido pedido = buscarPedidoPorIdUseCase.executar(pedidoId);
        
        // 2. Validar se o pedido pode ser finalizado
        if (!pedido.podeSerConcluido()) {
            throw new IllegalStateException("Pedido não pode ser finalizado. Status atual: " + pedido.getStatus());
        }

        // 3. Validar estoque
        validarEstoquePedidoUseCase.executar(pedido.getItens());
        
        // 4. Preparar lista de produtos para atualização
        List<Produto> produtosParaAtualizar = new ArrayList<>();
        
        for (PedidoItem item : pedido.getItens()) {
            Produto produto = buscarProdutoPorIdUseCase.executar(item.getIdProduto());
            
            // Criar cópia do produto com quantidade atualizada
            Produto produtoAtualizado = Produto.of(
                produto.getId(),
                produto.getNome(),
                produto.getSku().getValue(),
                produto.getDescricao() != null ? produto.getDescricao().getValue() : null,
                produto.getTag(),
                produto.getQuantidade().getValue() - item.getQuantidade().getValor(),
                produto.getPrecoUnitario().getValue(),
                produto.getCatalogo(),
                produto.getValorVenda().getValue(),
                produto.getImagemUrl() != null ? produto.getImagemUrl().getValue() : null,
                produto.getIdMarca()
            );
            
            produtosParaAtualizar.add(produtoAtualizado);
        }

        // 5. Atualizar quantidades dos produtos
        for (Produto produto : produtosParaAtualizar) {
            atualizarProdutoUseCase.executar(
                produto.getId(),
                produto.getNome(),
                produto.getSku().getValue(),
                produto.getDescricao() != null ? produto.getDescricao().getValue() : null,
                produto.getTag(),
                produto.getQuantidade().getValue(),
                produto.getPrecoUnitario().getValue(),
                produto.getCatalogo(),
                produto.getValorVenda().getValue(),
                produto.getImagemUrl() != null ? produto.getImagemUrl().getValue() : null,
                produto.getIdMarca()
            );
        }

        // 6. Alterar status do pedido para CONCLUIDO
        alterarStatusPedidoUseCase.executar(pedidoId, StatusPedido.CONCLUIDO);

        // 7. Calcular valor total da venda
        String valorTotalVenda = pedido.calcularTotal().getValor().toString();
        String descontoStr = desconto.toString();

        // 8. Criar a venda
        Venda venda = criarVendaUseCase.executar(
            valorTotalVenda,
            descontoStr,
            dataVenda,
            List.of(pedidoId)
        );

        return venda;
    }
}
