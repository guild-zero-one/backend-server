package com.zeroone.simlady.core.application.usecases.fornecedor;

import com.zeroone.simlady.core.application.ports.FornecedorRepositoryPort;
import com.zeroone.simlady.core.domain.fornecedor.Fornecedor;

public class CriarFornecedorUseCase {
    private final FornecedorRepositoryPort repository;

    public CriarFornecedorUseCase(FornecedorRepositoryPort repository) {
        this.repository = repository;
    }

    public Fornecedor executar(Fornecedor fornecedor) {
        return repository.salvarFornecedor(fornecedor);
    }
}
