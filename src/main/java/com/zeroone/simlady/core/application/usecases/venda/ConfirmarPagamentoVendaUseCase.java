package com.zeroone.simlady.core.application.usecases.venda;

import com.zeroone.simlady.core.application.ports.VendaRepositoryPort;
import com.zeroone.simlady.core.domain.venda.Venda;

import java.util.Optional;
import java.util.UUID;

public class ConfirmarPagamentoVendaUseCase {
    private final VendaRepositoryPort repository;

    public ConfirmarPagamentoVendaUseCase(VendaRepositoryPort repository) {
        this.repository = repository;
    }

    public Optional<Venda> executar(UUID id) {
        Optional<Venda> vendaOpt = repository.buscarPorId(id);
        
        if (vendaOpt.isEmpty()) {
            return Optional.empty();
        }
        
        Venda venda = vendaOpt.get();
        venda.confirmarPagamento();
        
        return Optional.of(repository.atualizarVenda(venda));
    }
}
