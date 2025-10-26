package com.zeroone.simlady.core.application.usecases.relatorio;

import com.zeroone.simlady.core.application.ports.RelatorioRepositoryPort;
import com.zeroone.simlady.core.domain.relatorio.ProdutoMaisVendido;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ListarProdutosMaisVendidosUseCase {
    
    private final RelatorioRepositoryPort relatorioRepository;
    
    public List<ProdutoMaisVendido> executar() {
        return relatorioRepository.buscarProdutosMaisVendidos();
    }
}
