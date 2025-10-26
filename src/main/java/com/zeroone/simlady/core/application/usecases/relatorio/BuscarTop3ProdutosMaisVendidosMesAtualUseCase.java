package com.zeroone.simlady.core.application.usecases.relatorio;

import com.zeroone.simlady.core.application.ports.RelatorioRepositoryPort;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
public class BuscarTop3ProdutosMaisVendidosMesAtualUseCase {
    
    private final RelatorioRepositoryPort relatorioRepository;
    
    public List<String> executar() {
        LocalDate inicioMes = LocalDate.now().withDayOfMonth(1);
        LocalDate fimMes = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
        
        List<String> produtos = relatorioRepository.buscarTop3ProdutosMaisVendidosMesAtual(inicioMes, fimMes);
        return produtos.size() > 3 ? produtos.subList(0, 3) : produtos;
    }
}
