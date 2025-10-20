package com.zeroone.simlady.core.application.usecases.fornecedor;

import com.zeroone.simlady.core.application.ports.FornecedorRepositoryPort;

import java.util.UUID;

public class DeletarFornecedorPorId {
    private final FornecedorRepositoryPort repository;

    public DeletarFornecedorPorId(FornecedorRepositoryPort repository) {
        this.repository = repository;
    }

    public void executar(UUID id) {
        repository.deletarPorId(id);
    }

}
