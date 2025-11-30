package com.zeroone.simlady.core.application.usecases.relatorio;

import com.zeroone.simlady.core.application.ports.RelatorioRepositoryPort;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@RequiredArgsConstructor
public class ObterFaturamentoUltimos4MesesUseCase {
    
    private final RelatorioRepositoryPort relatorioRepository;
    
    public Map<String, BigDecimal> executar() {
        LocalDate inicio = LocalDate.now().minusMonths(3).withDayOfMonth(1);
        return relatorioRepository.obterFaturamentoUltimos4Meses(inicio);
    }
}

