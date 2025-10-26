package com.zeroone.simlady.core.application.usecases.venda;

import com.zeroone.simlady.core.application.ports.VendaRepositoryPort;
import com.zeroone.simlady.core.domain.venda.Venda;

import java.util.Optional;
import java.util.UUID;

public class BuscarVendaPorIdUseCase {
    private final VendaRepositoryPort repository;

    public BuscarVendaPorIdUseCase(VendaRepositoryPort repository) {
        this.repository = repository;
    }

    public Optional<Venda> executar(UUID id) {
        return repository.buscarPorId(id);
    }
}
