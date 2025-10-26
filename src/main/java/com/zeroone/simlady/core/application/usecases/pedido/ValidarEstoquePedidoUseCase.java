package com.zeroone.simlady.core.application.usecases.pedido;

import com.zeroone.simlady.core.application.usecases.produto.BuscarProdutoPorIdUseCase;
import com.zeroone.simlady.core.domain.pedido.PedidoItem;
import com.zeroone.simlady.core.domain.produto.Produto;
import com.zeroone.simlady.infrastructure.exception.ResourceAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ValidarEstoquePedidoUseCase {

    private final BuscarProdutoPorIdUseCase buscarProdutoPorIdUseCase;

    public void executar(List<PedidoItem> itens) {
        if (itens == null || itens.isEmpty()) {
            return; // Não há itens para validar
        }

        for (PedidoItem item : itens) {
            Produto produto = buscarProdutoPorIdUseCase.executar(item.getIdProduto());
            
            // Validar se há estoque suficiente
            if (produto.getQuantidade().getValue() < item.getQuantidade().getValor()) {
                throw new ResourceAlreadyExistsException(
                    String.format("Estoque insuficiente para o produto: %s. Disponível: %d, Solicitado: %d",
                        produto.getNome(),
                        produto.getQuantidade().getValue(),
                        item.getQuantidade().getValor()
                    )
                );
            }
        }
    }
}
