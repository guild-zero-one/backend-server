package com.zeroone.simlady.core.application.usecases.relatorio;

import com.zeroone.simlady.core.application.ports.RelatorioRepositoryPort;
import com.zeroone.simlady.core.domain.relatorio.ResumoVendasProduto;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@RequiredArgsConstructor
public class ObterResumoVendasProdutoUseCase {
    
    private final RelatorioRepositoryPort relatorioRepository;
    
    public ResumoVendasProduto executar(UUID produtoId) {
        LocalDate inicioMes = LocalDate.now().withDayOfMonth(1);
        LocalDate fimMes = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
        
        return relatorioRepository.obterResumoVendasProduto(produtoId, inicioMes, fimMes);
    }
}
