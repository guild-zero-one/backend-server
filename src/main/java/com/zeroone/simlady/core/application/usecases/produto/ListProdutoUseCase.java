package com.zeroone.simlady.core.application.usecases.produto;

import com.zeroone.simlady.core.application.ports.ProdutoRepository;
import com.zeroone.simlady.core.domain.produto.Produto;

import java.util.List;

public class ListProdutoUseCase {
    private final ProdutoRepository repository;

    public ListProdutoUseCase(ProdutoRepository repository) {
        this.repository = repository;
    }

    public List<Produto> executar(){
        return repository.listar();
    }
}
