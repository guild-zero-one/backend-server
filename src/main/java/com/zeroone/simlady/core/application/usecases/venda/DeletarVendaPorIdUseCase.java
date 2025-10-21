package com.zeroone.simlady.core.application.usecases.venda;

import com.zeroone.simlady.core.application.ports.VendaRepositoryPort;

import java.util.UUID;

public class DeletarVendaPorIdUseCase {
    private final VendaRepositoryPort repository;

    public DeletarVendaPorIdUseCase(VendaRepositoryPort repository) {
        this.repository = repository;
    }

    public void executar(UUID id) {
        repository.deletarPorId(id);
    }
}
