package com.zeroone.simlady.core.application.usecases.produto;

import com.zeroone.simlady.core.application.ports.ProdutoRepository;

import java.util.UUID;

public class DeletarProdutoPorIdUseCase {
    private final ProdutoRepository repository;

    public DeletarProdutoPorIdUseCase(ProdutoRepository repository) {
        this.repository = repository;
    }

    public void executar(UUID id) {
        repository.deletarPorId(id);
    }
}
