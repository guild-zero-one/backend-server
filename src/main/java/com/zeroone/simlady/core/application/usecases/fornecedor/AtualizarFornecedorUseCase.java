package com.zeroone.simlady.core.application.usecases.fornecedor;

import com.zeroone.simlady.core.application.ports.FornecedorRepositoryPort;
import com.zeroone.simlady.core.domain.fornecedor.Fornecedor;


public class AtualizarFornecedorUseCase {
    private final FornecedorRepositoryPort repository;

    public AtualizarFornecedorUseCase(FornecedorRepositoryPort repository) {
        this.repository = repository;
    }

    public Fornecedor executar(Fornecedor fornecedor) {
        Fornecedor fornecedorAtualizado = repository.atualizarFornecedor(fornecedor);
        return fornecedorAtualizado;
    }
}
