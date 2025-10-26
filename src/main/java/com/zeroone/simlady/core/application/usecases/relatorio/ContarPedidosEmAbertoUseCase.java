package com.zeroone.simlady.core.application.usecases.relatorio;

import com.zeroone.simlady.core.application.ports.RelatorioRepositoryPort;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ContarPedidosEmAbertoUseCase {
    
    private final RelatorioRepositoryPort relatorioRepository;
    
    public Integer executar() {
        return relatorioRepository.contarPedidosEmAberto();
    }
}
