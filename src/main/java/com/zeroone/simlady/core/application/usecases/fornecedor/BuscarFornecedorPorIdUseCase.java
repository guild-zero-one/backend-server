package com.zeroone.simlady.core.application.usecases.fornecedor;

import com.zeroone.simlady.core.application.ports.FornecedorRepositoryPort;
import com.zeroone.simlady.core.domain.fornecedor.Fornecedor;

import java.util.UUID;

public class BuscarFornecedorPorIdUseCase {

    private final FornecedorRepositoryPort repository;

    public BuscarFornecedorPorIdUseCase(FornecedorRepositoryPort repository) {
        this.repository = repository;
    }

    public Fornecedor executar(UUID id) {
        return repository.buscarPorId(id).orElseThrow(() -> new RuntimeException("Fornecedor não encontrado com ID: " + id));
    }
}
