package com.zeroone.simlady.core.application.usecases.produto;

import com.zeroone.simlady.core.application.ports.ProdutoRepositoryPort;
import com.zeroone.simlady.core.domain.produto.Produto;

import java.util.Optional;
import java.util.UUID;

public class AtualizarProdutoUseCase {
    private final ProdutoRepositoryPort repository;

    public AtualizarProdutoUseCase(ProdutoRepositoryPort repository) {
        this.repository = repository;
    }

    public Optional<Produto> executar(UUID id, String nome, String sku, String descricao, 
                                     String tag, Integer quantidade, Double precoUnitario, 
                                     Boolean catalogo, Double valorVenda, String imagemUrl) {
        Optional<Produto> produtoOpt = repository.buscarPorId(id);
        
        if (produtoOpt.isEmpty()) {
            return Optional.empty();
        }
        
        Produto produtoExistente = produtoOpt.get();
        
        Produto produtoAtualizado = Produto.of(
                produtoExistente.getId(),
                nome != null ? nome : produtoExistente.getNome(),
                sku != null ? sku : produtoExistente.getSku().getValue(),
                descricao != null ? descricao : produtoExistente.getDescricao().getValue(),
                tag != null ? tag : produtoExistente.getTag(),
                quantidade != null ? quantidade : produtoExistente.getQuantidade().getValue(),
                precoUnitario != null ? precoUnitario : produtoExistente.getPrecoUnitario().getValue(),
                catalogo != null ? catalogo : produtoExistente.getCatalogo(),
                valorVenda != null ? valorVenda : produtoExistente.getValorVenda().getValue(),
                imagemUrl != null ? imagemUrl : (produtoExistente.getImagemUrl() != null ? produtoExistente.getImagemUrl().getValue() : null),
                produtoExistente.getIdMarca()
        );
        
        return Optional.of(repository.atualizarProduto(produtoAtualizado));
    }
}
