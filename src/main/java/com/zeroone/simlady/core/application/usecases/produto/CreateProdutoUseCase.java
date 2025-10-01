package com.zeroone.simlady.core.application.usecases.produto;

import com.zeroone.simlady.core.application.ports.ProdutoRepository;
import com.zeroone.simlady.core.domain.produto.Produto;

public class CreateProdutoUseCase {
    private final ProdutoRepository repository;

    public CreateProdutoUseCase(ProdutoRepository repository) {
        this.repository = repository;
    }

    public Produto executar(Produto produto){
        return repository.salvarProduto(produto);
    }
}
