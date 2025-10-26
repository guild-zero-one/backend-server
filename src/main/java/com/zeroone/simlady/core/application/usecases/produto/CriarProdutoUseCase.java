package com.zeroone.simlady.core.application.usecases.produto;

import com.zeroone.simlady.core.application.ports.ProdutoRepositoryPort;
import com.zeroone.simlady.core.domain.produto.Produto;

public class CriarProdutoUseCase {
    private final ProdutoRepositoryPort repository;

    public CriarProdutoUseCase(ProdutoRepositoryPort repository) {
        this.repository = repository;
    }

    public Produto executar(Produto produto) {
        return repository.salvarProduto(produto);
    }
}