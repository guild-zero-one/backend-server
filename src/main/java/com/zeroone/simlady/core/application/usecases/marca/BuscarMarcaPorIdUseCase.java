package com.zeroone.simlady.core.application.usecases.marca;

import com.zeroone.simlady.core.application.ports.MarcaRepositoryPort;
import com.zeroone.simlady.core.domain.marca.Marca;

import java.util.UUID;

public class BuscarMarcaPorIdUseCase {

    private final MarcaRepositoryPort repository;

    public BuscarMarcaPorIdUseCase(MarcaRepositoryPort repository) {
        this.repository = repository;
    }

    public Marca executar(UUID id) {
        return repository.buscarPorId(id).orElseThrow(() -> new RuntimeException("Marca não encontrada com ID: " + id));
    }
}
