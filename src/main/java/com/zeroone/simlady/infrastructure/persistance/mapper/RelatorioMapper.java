package com.zeroone.simlady.infrastructure.persistance.mapper;

import com.zeroone.simlady.core.adapters.dtos.relatorio.ProdutosMaisVendidosResponseDto;
import com.zeroone.simlady.core.adapters.dtos.relatorio.ResumoVendasProdutoResponseDto;
import com.zeroone.simlady.core.domain.relatorio.ProdutoMaisVendido;
import com.zeroone.simlady.core.domain.relatorio.ResumoVendasProduto;

public class RelatorioMapper {

    public static ProdutosMaisVendidosResponseDto toDto(ProdutoMaisVendido produto) {
        return new ProdutosMaisVendidosResponseDto(
                produto.getId(),
                produto.getNome(),
                produto.getQuantidadeVendida(),
                produto.getValorTotalVendido()
        );
    }

    public static ResumoVendasProdutoResponseDto toDto(ResumoVendasProduto resumo) {
        return new ResumoVendasProdutoResponseDto(
                resumo.getVendasMesAtual(),
                resumo.getVendasTotais()
        );
    }
}
