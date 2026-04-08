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
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import static java.util.Locale.forLanguageTag;

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

    public ResumoVendasProdutoResponseDto obterResumoVendasProduto(UUID produtoId) {
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

    public Map<String, Integer> getQuantidadePedidosUltimos6Meses() {
        LocalDate start = LocalDate.now().minusMonths(5).withDayOfMonth(1);
        List<Object[]> results = vendaRepository.countPedidosPorMesUltimos6Meses(start);
        Map<String, Integer> quantidadePorMes = new LinkedHashMap<>();
        DateTimeFormatter parser = DateTimeFormatter.ofPattern("yyyy-MM");
        DateTimeFormatter mesFormatter = DateTimeFormatter.ofPattern("MMMM", forLanguageTag("pt-BR"));

        for (Object[] row : results) {
            String mesAno = ((String) row[0]).trim();
            Integer quantidade = ((Number) row[1]).intValue();

            String mesBonito = YearMonth.parse(mesAno, parser).format(mesFormatter);
            mesBonito = mesBonito.substring(0, 1).toUpperCase() + mesBonito.substring(1);

            quantidadePorMes.put(mesBonito, quantidade);
        }
        return quantidadePorMes;
    }

    public Map<String, BigDecimal> getFaturamentoUltimos6Meses() {
        LocalDate start = LocalDate.now().minusMonths(5).withDayOfMonth(1);
        List<Object[]> results = vendaRepository.sumValorTotalPorMesUltimos6Meses(start);
        Map<String, BigDecimal> faturamentoPorMes = new LinkedHashMap<>();

        DateTimeFormatter parser = DateTimeFormatter.ofPattern("yyyy-MM");
        DateTimeFormatter mesFormatter = DateTimeFormatter.ofPattern("MMMM", Locale.forLanguageTag("pt-BR"));

        for (Object[] row : results) {
            String mesAno = ((String) row[0]).trim(); // Exemplo: "2025-06"
            BigDecimal total = (BigDecimal) row[1];

            String mesBonito = YearMonth.parse(mesAno, parser).format(mesFormatter);
            mesBonito = mesBonito.substring(0, 1).toUpperCase() + mesBonito.substring(1);

            faturamentoPorMes.put(mesBonito, total);
        }
        return faturamentoPorMes;
    }

    public Integer pedidosEmAberto() {
        return pedidoVendaRepository.countByStatus(StatusPedido.PENDENTE);
    }
}