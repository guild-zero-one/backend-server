package com.zeroone.simlady.core.application.usecases.relatorio;

import com.zeroone.simlady.core.application.ports.RelatorioRepositoryPort;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.Map;

@RequiredArgsConstructor
public class ObterQuantidadePedidosUltimos6MesesUseCase {
    
    private final RelatorioRepositoryPort relatorioRepository;
    
    public Map<String, Integer> executar() {
        LocalDate inicio = LocalDate.now().minusMonths(5).withDayOfMonth(1);
        return relatorioRepository.obterQuantidadePedidosUltimos6Meses(inicio);
    }
}
