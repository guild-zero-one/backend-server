package com.zeroone.simlady.service;

import com.zeroone.simlady.dto.produto.ProdutosMaisVendidosResponseDto;
import com.zeroone.simlady.dto.relatorio.ResumoVendasProdutoResponseDto;
import com.zeroone.simlady.entity.enums.StatusPedido;
import com.zeroone.simlady.repository.PedidoItemRepository;
import com.zeroone.simlady.repository.PedidoVendaRepository;
import com.zeroone.simlady.repository.VendaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RelatorioService {
    private final PedidoItemRepository pedidoItemRepository;
    private final ProdutoService produtoService;
    private final VendaRepository vendaRepository;
    private final PedidoVendaRepository pedidoVendaRepository;

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

    public BigDecimal totalVendasMesAtual() {
        LocalDate start = LocalDate.now().withDayOfMonth(1);
        LocalDate end = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
        BigDecimal total = vendaRepository.sumValorTotalByDataVendaBetween(start, end);
        return total != null ? total : BigDecimal.ZERO;
    }

    public List<String> top3NomesProdutosMaisVendidosMesAtual() {
        LocalDate start = LocalDate.now().withDayOfMonth(1);
        LocalDate end = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
        List<String> nomes = vendaRepository.findTop3NomesProdutosMaisVendidosNoMes(start, end);
        return nomes.size() > 3 ? nomes.subList(0, 3) : nomes;
    }

    public Integer quantidadeProdutosVendidosUltimos6Meses() {
        LocalDate end = LocalDate.now();
        LocalDate start = end.minusMonths(6).withDayOfMonth(1);
        Integer quantidade = vendaRepository.quantidadeProdutosVendidosUltimos6Meses(start, end);
        return quantidade != null ? quantidade : 0;
    }

    public List<BigDecimal> valoresVendasUltimos6Meses() {
        LocalDate start = LocalDate.now().minusMonths(5).withDayOfMonth(1);
        return vendaRepository.sumValorTotalUltimos6Meses(start);
    }

    public Integer pedidosEmAberto() {
        return pedidoVendaRepository.countByStatus(StatusPedido.PENDENTE);
    }
}