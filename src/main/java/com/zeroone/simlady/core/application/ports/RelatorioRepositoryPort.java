package com.zeroone.simlady.core.application.ports;

import com.zeroone.simlady.core.domain.relatorio.ProdutoMaisVendido;
import com.zeroone.simlady.core.domain.relatorio.ResumoVendasProduto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface RelatorioRepositoryPort {
    
    List<ProdutoMaisVendido> buscarProdutosMaisVendidos();
    
    ResumoVendasProduto obterResumoVendasProduto(UUID produtoId, LocalDate inicioMes, LocalDate fimMes);
    
    BigDecimal calcularTotalVendasMesAtual(LocalDate inicioMes, LocalDate fimMes);
    
    List<String> buscarTop3ProdutosMaisVendidosMesAtual(LocalDate inicioMes, LocalDate fimMes);
    
    Map<String, Integer> obterQuantidadePedidosUltimos6Meses(LocalDate inicio);
    
    Map<String, BigDecimal> obterFaturamentoUltimos6Meses(LocalDate inicio);
    
    Integer contarPedidosEmAberto();
}
