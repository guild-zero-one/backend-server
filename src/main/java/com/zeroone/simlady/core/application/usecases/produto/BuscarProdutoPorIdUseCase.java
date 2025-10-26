package com.zeroone.simlady.core.application.usecases.produto;

import com.zeroone.simlady.core.application.ports.ProdutoRepositoryPort;
import com.zeroone.simlady.core.domain.produto.Produto;
import com.zeroone.simlady.infrastructure.exception.ResourceNotFoundException;

import java.util.UUID;

public class BuscarProdutoPorIdUseCase {

    private final ProdutoRepositoryPort repository;

    public BuscarProdutoPorIdUseCase(ProdutoRepositoryPort repository) {
        this.repository = repository;
    }

    public Produto executar(UUID id) {
        return repository.buscarPorId(id).orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com ID: " + id));
    }
}
