package com.zeroone.simlady.core.application.usecases.produto;

import com.zeroone.simlady.core.application.ports.ProdutoRepository;
import com.zeroone.simlady.core.domain.produto.Produto;

import java.util.Optional;
import java.util.UUID;

public class AlterarStatusCatalogoUseCase {
    private final ProdutoRepository repository;

    public AlterarStatusCatalogoUseCase(ProdutoRepository repository) {
        this.repository = repository;
    }

    public Optional<Produto> executar(UUID id, Boolean novoStatus) {
        Optional<Produto> produtoOpt = repository.buscarPorId(id);
        
        if (produtoOpt.isEmpty()) {
            return Optional.empty();
        }
        
        Produto produtoExistente = produtoOpt.get();
        
        Produto produtoAtualizado = Produto.of(
                produtoExistente.getId(),
                produtoExistente.getNome(),
                produtoExistente.getSku().getValue(),
                produtoExistente.getDescricao().getValue(),
                produtoExistente.getTag(),
                produtoExistente.getQuantidade().getValue(),
                produtoExistente.getPrecoUnitario().getValue(),
                novoStatus,
                produtoExistente.getValorVenda().getValue(),
                produtoExistente.getImagemUrl() != null ? produtoExistente.getImagemUrl().getValue() : null,
                produtoExistente.getIdFornecedor()
        );
        
        return Optional.of(repository.atualizarProduto(produtoAtualizado));
    }
}