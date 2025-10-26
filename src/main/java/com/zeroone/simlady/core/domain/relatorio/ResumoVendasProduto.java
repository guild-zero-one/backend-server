package com.zeroone.simlady.core.domain.relatorio;

public class ResumoVendasProduto {
    private Integer vendasMesAtual;
    private Integer vendasTotais;

    private ResumoVendasProduto(Integer vendasMesAtual, Integer vendasTotais) {
        this.vendasMesAtual = vendasMesAtual;
        this.vendasTotais = vendasTotais;
    }

    public static ResumoVendasProduto of(Integer vendasMesAtual, Integer vendasTotais) {
        return new ResumoVendasProduto(vendasMesAtual, vendasTotais);
    }

    public Integer getVendasMesAtual() {
        return vendasMesAtual;
    }

    public Integer getVendasTotais() {
        return vendasTotais;
    }
}
