package com.zeroone.simlady.core.domain.relatorio;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class RelatorioVendas {
    private BigDecimal totalVendasMesAtual;
    private List<String> top3ProdutosMaisVendidos;
    private Map<String, Integer> quantidadePedidosUltimos6Meses;
    private Map<String, BigDecimal> faturamentoUltimos6Meses;
    private Integer pedidosEmAberto;

    private RelatorioVendas(BigDecimal totalVendasMesAtual, List<String> top3ProdutosMaisVendidos,
                           Map<String, Integer> quantidadePedidosUltimos6Meses,
                           Map<String, BigDecimal> faturamentoUltimos6Meses,
                           Integer pedidosEmAberto) {
        this.totalVendasMesAtual = totalVendasMesAtual;
        this.top3ProdutosMaisVendidos = top3ProdutosMaisVendidos;
        this.quantidadePedidosUltimos6Meses = quantidadePedidosUltimos6Meses;
        this.faturamentoUltimos6Meses = faturamentoUltimos6Meses;
        this.pedidosEmAberto = pedidosEmAberto;
    }

    public static RelatorioVendas of(BigDecimal totalVendasMesAtual, List<String> top3ProdutosMaisVendidos,
                                   Map<String, Integer> quantidadePedidosUltimos6Meses,
                                   Map<String, BigDecimal> faturamentoUltimos6Meses,
                                   Integer pedidosEmAberto) {
        return new RelatorioVendas(totalVendasMesAtual, top3ProdutosMaisVendidos,
                quantidadePedidosUltimos6Meses, faturamentoUltimos6Meses, pedidosEmAberto);
    }

    public BigDecimal getTotalVendasMesAtual() {
        return totalVendasMesAtual;
    }

    public List<String> getTop3ProdutosMaisVendidos() {
        return top3ProdutosMaisVendidos;
    }

    public Map<String, Integer> getQuantidadePedidosUltimos6Meses() {
        return quantidadePedidosUltimos6Meses;
    }

    public Map<String, BigDecimal> getFaturamentoUltimos6Meses() {
        return faturamentoUltimos6Meses;
    }

    public Integer getPedidosEmAberto() {
        return pedidosEmAberto;
    }
}
