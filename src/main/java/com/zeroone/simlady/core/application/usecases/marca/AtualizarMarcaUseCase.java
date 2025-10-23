package com.zeroone.simlady.core.application.usecases.marca;

import com.zeroone.simlady.core.application.ports.MarcaRepositoryPort;
import com.zeroone.simlady.core.domain.marca.Marca;


public class AtualizarMarcaUseCase {
    private final MarcaRepositoryPort repository;

    public AtualizarMarcaUseCase(MarcaRepositoryPort repository) {
        this.repository = repository;
    }

    public Marca executar(Marca marca) {
        return repository.atualizarMarca(marca);
    }
}
