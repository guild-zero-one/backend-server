package com.zeroone.simlady.service;

import com.zeroone.simlady.dto.produto.ProdutosMaisVendidosResponseDto;
import com.zeroone.simlady.dto.relatorio.ResumoVendasProdutoResponseDto;
import com.zeroone.simlady.repository.PedidoItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RelatorioService {
    private final PedidoItemRepository pedidoItemRepository;
    private final ProdutoService produtoService;

    public List<ProdutosMaisVendidosResponseDto> listarVendasPorProduto() {
        return pedidoItemRepository.buscarProdutosMaisVendidos();
    }

    public ResumoVendasProdutoResponseDto obterResumoVendasProduto(Integer produtoId) {
        produtoService.buscarPorId(produtoId);

        LocalDate inicioMesAtual = LocalDate.now().withDayOfMonth(1);
        LocalDate fimMesAtual = LocalDate.now().withDayOfMonth(
                LocalDate.now().lengthOfMonth()
        );

        Integer vendasMesAtual = pedidoItemRepository.countVendasProdutoPeriodo(
                produtoId,
                inicioMesAtual,
                fimMesAtual
        );

        Integer vendasTotais = pedidoItemRepository.countVendasTotaisProduto(produtoId);

        ResumoVendasProdutoResponseDto resumo = new ResumoVendasProdutoResponseDto();
        resumo.setVendasMesAtual(vendasMesAtual);
        resumo.setVendasTotais(vendasTotais);

        return resumo;
    }
}