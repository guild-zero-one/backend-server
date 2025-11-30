package com.zeroone.simlady.infrastructure.persistance.adapter;

import com.zeroone.simlady.core.application.ports.RelatorioRepositoryPort;
import com.zeroone.simlady.core.domain.pedido.StatusPedido;
import com.zeroone.simlady.core.domain.relatorio.ProdutoMaisVendido;
import com.zeroone.simlady.core.domain.relatorio.ResumoVendasProduto;
import com.zeroone.simlady.infrastructure.persistance.repository.PedidoItemRepositoryImpl;
import com.zeroone.simlady.infrastructure.persistance.repository.PedidoRepositoryImpl;
import com.zeroone.simlady.infrastructure.persistance.repository.VendaRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static java.util.Locale.forLanguageTag;

@Repository
@RequiredArgsConstructor
public class RelatorioJpaAdapter implements RelatorioRepositoryPort {

    private final PedidoItemRepositoryImpl pedidoItemRepository;
    private final VendaRepositoryImpl vendaRepository;
    private final PedidoRepositoryImpl pedidoVendaRepository;

    @Override
    public List<ProdutoMaisVendido> buscarProdutosMaisVendidos() {
        return pedidoItemRepository.buscarProdutosMaisVendidosPorStatus(StatusPedido.CONCLUIDO).stream()
                .map((Object[] row) -> {
                    UUID idProduto = (UUID) row[0];
                    String nome = (String) row[1];
                    Long quantidade = ((Number) row[2]).longValue(); 
                    BigDecimal valorTotal = (BigDecimal) row[3];

                    
                    return ProdutoMaisVendido.of(
                            idProduto,
                            nome,
                            quantidade.intValue(),
                            valorTotal
                    );
                })
                .toList();
    }

    @Override
    public List<ProdutoMaisVendido> buscarProdutosMaisVendidosMesAtual(LocalDate inicioMes, LocalDate fimMes) {
        LocalDate fimMesExclusivo = fimMes.plusDays(1);
        return pedidoItemRepository.buscarProdutosMaisVendidosPorStatusEPeriodo(
                StatusPedido.CONCLUIDO, inicioMes, fimMesExclusivo
        ).stream()
                .map((Object[] row) -> {
                    UUID idProduto = (UUID) row[0];
                    String nome = (String) row[1];
                    Long quantidade = ((Number) row[2]).longValue(); 
                    BigDecimal valorTotal = (BigDecimal) row[3];

                    return ProdutoMaisVendido.of(
                            idProduto,
                            nome,
                            quantidade.intValue(),
                            valorTotal
                    );
                })
                .toList();
    }

    @Override
    public ResumoVendasProduto obterResumoVendasProduto(UUID produtoId, LocalDate inicioMes, LocalDate fimMes) {
        Integer vendasMesAtual = pedidoItemRepository.countVendasProdutoPeriodo(
                produtoId, inicioMes, fimMes);
        Integer vendasTotais = pedidoItemRepository.countVendasTotaisProduto(produtoId);
        
        return ResumoVendasProduto.of(vendasMesAtual, vendasTotais);
    }

    @Override
    public BigDecimal calcularTotalVendasMesAtual(LocalDate inicioMes, LocalDate fimMes) {
        BigDecimal total = vendaRepository.sumValorTotalByDataVendaBetween(inicioMes, fimMes);
        return total != null ? total : BigDecimal.ZERO;
    }

    @Override
    public List<String> buscarTop3ProdutosMaisVendidosMesAtual(LocalDate inicioMes, LocalDate fimMes) {
        return vendaRepository.findTop3NomesProdutosMaisVendidosNoMes(inicioMes, fimMes).stream()
                .map(id -> "Produto " + id)
                .toList();
    }

    @Override
    public Map<String, Integer> obterQuantidadePedidosUltimos6Meses(LocalDate inicio) {
        List<Object[]> results = vendaRepository.countPedidosPorMesUltimos6Meses(inicio);
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

    @Override
    public Map<String, BigDecimal> obterFaturamentoUltimos6Meses(LocalDate inicio) {
        List<Object[]> results = vendaRepository.sumValorTotalPorMesUltimos6Meses(inicio);
        Map<String, BigDecimal> faturamentoPorMes = new LinkedHashMap<>();

        DateTimeFormatter parser = DateTimeFormatter.ofPattern("yyyy-MM");
        DateTimeFormatter mesFormatter = DateTimeFormatter.ofPattern("MMMM", Locale.forLanguageTag("pt-BR"));

        for (Object[] row : results) {
            String mesAno = ((String) row[0]).trim();
            BigDecimal total = (BigDecimal) row[1];

            String mesBonito = YearMonth.parse(mesAno, parser).format(mesFormatter);
            mesBonito = mesBonito.substring(0, 1).toUpperCase() + mesBonito.substring(1);

            faturamentoPorMes.put(mesBonito, total);
        }
        return faturamentoPorMes;
    }

    @Override
    public Map<String, BigDecimal> obterFaturamentoUltimos4Meses(LocalDate inicio) {
        List<Object[]> results = vendaRepository.sumValorTotalPorMesUltimos4Meses(inicio);
        Map<String, BigDecimal> faturamentoPorMes = new LinkedHashMap<>();

        DateTimeFormatter parser = DateTimeFormatter.ofPattern("yyyy-MM");
        DateTimeFormatter mesFormatter = DateTimeFormatter.ofPattern("MMMM", Locale.forLanguageTag("pt-BR"));

        for (Object[] row : results) {
            String mesAno = ((String) row[0]).trim();
            BigDecimal total = (BigDecimal) row[1];

            String mesBonito = YearMonth.parse(mesAno, parser).format(mesFormatter);
            mesBonito = mesBonito.substring(0, 1).toUpperCase() + mesBonito.substring(1);

            faturamentoPorMes.put(mesBonito, total);
        }
        return faturamentoPorMes;
    }

    @Override
    public Map<StatusPedido, Integer> obterPedidosPorStatusMesAtual(LocalDate inicioMes, LocalDate fimMes) {
        LocalDateTime inicio = inicioMes.atStartOfDay();
        LocalDateTime fim = fimMes.atStartOfDay();
        
        List<Object[]> results = pedidoVendaRepository.countPedidosPorStatusNoPeriodo(inicio, fim);
        Map<StatusPedido, Integer> pedidosPorStatus = new HashMap<>();
        
        for (Object[] row : results) {
            StatusPedido status = (StatusPedido) row[0];
            Long count = ((Number) row[1]).longValue();
            pedidosPorStatus.put(status, count.intValue());
        }
        
        // Garantir que todos os status estejam presentes, mesmo com 0
        for (StatusPedido status : StatusPedido.values()) {
            pedidosPorStatus.putIfAbsent(status, 0);
        }
        
        return pedidosPorStatus;
    }

    @Override
    public Integer contarPedidosEmAberto() {
        return (int) pedidoVendaRepository.countByStatus(com.zeroone.simlady.core.domain.pedido.StatusPedido.PENDENTE);
    }

    // Método para debug - verificar dados no banco
    public Map<String, Long> verificarDadosBanco() {
        Map<String, Long> dados = new HashMap<>();
        dados.put("totalPedidoItems", pedidoItemRepository.countTotalPedidoItems());
        dados.put("pedidosConcluidos", pedidoItemRepository.countPedidosConcluidos());
        dados.put("pedidoItemsComPedidosConcluidos", pedidoItemRepository.countPedidoItemsComPedidosConcluidos());
        return dados;
    }

    // Método para debug - verificar dados de vendas no período
    public Map<String, Long> verificarDadosVendasNoPeriodo(LocalDate inicio, LocalDate fim) {
        Map<String, Long> dados = new HashMap<>();
        dados.put("vendasNoPeriodo", vendaRepository.countVendasNoPeriodo(inicio, fim));
        dados.put("pedidosComVenda", vendaRepository.countPedidosComVenda());
        dados.put("pedidoItemsComVenda", vendaRepository.countPedidoItemsComVenda());
        return dados;
    }
}
