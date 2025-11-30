package com.zeroone.simlady.core.application.usecases.relatorio;

import com.zeroone.simlady.core.application.ports.RelatorioRepositoryPort;
import com.zeroone.simlady.core.domain.pedido.StatusPedido;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.Map;

@RequiredArgsConstructor
public class ObterPedidosPorStatusMesAtualUseCase {
    
    private final RelatorioRepositoryPort relatorioRepository;
    
    public Map<StatusPedido, Integer> executar() {
        LocalDate agora = LocalDate.now();
        LocalDate inicioMes = agora.withDayOfMonth(1);
        LocalDate fimMes = agora.withDayOfMonth(agora.lengthOfMonth()).plusDays(1);
        
        return relatorioRepository.obterPedidosPorStatusMesAtual(inicioMes, fimMes);
    }
}

