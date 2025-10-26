package com.zeroone.simlady.core.application.usecases.relatorio;

import com.zeroone.simlady.core.application.ports.RelatorioRepositoryPort;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@RequiredArgsConstructor
public class CalcularTotalVendasMesAtualUseCase {
    
    private final RelatorioRepositoryPort relatorioRepository;
    
    public BigDecimal executar() {
        LocalDate inicioMes = LocalDate.now().withDayOfMonth(1);
        LocalDate fimMes = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
        
        return relatorioRepository.calcularTotalVendasMesAtual(inicioMes, fimMes);
    }
}
