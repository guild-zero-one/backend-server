package com.zeroone.simlady.core.application.usecases.produto;

import com.zeroone.simlady.core.application.ports.ProdutoRepositoryPort;
import com.zeroone.simlady.infrastructure.exception.ResourceNotFoundException;

import java.util.UUID;

public class DeletarProdutoPorIdUseCase {
    private final ProdutoRepositoryPort repository;

    public DeletarProdutoPorIdUseCase(ProdutoRepositoryPort repository) {
        this.repository = repository;
    }

    public void executar(UUID id) {
        repository.buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com ID: " + id));
        repository.deletarPorId(id);
    }
}
