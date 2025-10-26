package com.zeroone.simlady.core.application.usecases.marca;

import com.zeroone.simlady.core.application.ports.MarcaRepositoryPort;

import java.util.UUID;

public class DeletarMarcaPorIdUseCase {
    private final MarcaRepositoryPort repository;

    public DeletarMarcaPorIdUseCase(MarcaRepositoryPort repository) {
        this.repository = repository;
    }

    public void executar(UUID id) {
        repository.deletarPorId(id);
    }

}
