package com.zeroone.simlady.core.application.usecases.relatorio;

import com.zeroone.simlady.core.application.ports.RelatorioRepositoryPort;
import com.zeroone.simlady.core.domain.relatorio.ProdutoMaisVendido;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
public class ListarTop3ProdutosMaisVendidosMesAtualUseCase {
    
    private final RelatorioRepositoryPort relatorioRepository;
    
    public List<ProdutoMaisVendido> executar() {
        LocalDate agora = LocalDate.now();
        LocalDate inicioMes = agora.withDayOfMonth(1);
        LocalDate fimMes = agora.withDayOfMonth(agora.lengthOfMonth());
        
        List<ProdutoMaisVendido> produtos = relatorioRepository.buscarProdutosMaisVendidosMesAtual(inicioMes, fimMes);
        return produtos.size() > 3 ? produtos.subList(0, 3) : produtos;
    }
}

