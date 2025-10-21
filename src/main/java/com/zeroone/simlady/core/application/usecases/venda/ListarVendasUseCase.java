package com.zeroone.simlady.core.application.usecases.venda;

import com.zeroone.simlady.core.application.ports.VendaRepositoryPort;
import com.zeroone.simlady.core.domain.venda.Venda;
import org.springframework.data.domain.Page;

public class ListarVendasUseCase {
    private final VendaRepositoryPort repository;

    public ListarVendasUseCase(VendaRepositoryPort repository) {
        this.repository = repository;
    }

    public Page<Venda> executar(int pagina, int tamanho) {
        return repository.listarTodas(pagina, tamanho);
    }
}
