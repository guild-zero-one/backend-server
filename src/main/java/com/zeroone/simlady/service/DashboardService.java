package com.zeroone.simlady.service;

import com.zeroone.simlady.dto.dashboard.*;
import com.zeroone.simlady.entity.enums.StatusPedido;
import com.zeroone.simlady.repository.PedidoVendaRepository;
import com.zeroone.simlady.repository.ProdutoRepository;
import com.zeroone.simlady.repository.VendaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final ProdutoRepository produtoRepository;
    private final PedidoVendaRepository pedidoVendaRepository;
    private final VendaRepository vendaRepository;

    public HomeKpisResponseDto obterHomeKpis() {
        Long totalItensEstoque = produtoRepository.sumQuantidadeTotalEmEstoque();
        Integer pedidosPendentes = pedidoVendaRepository.countByStatus(StatusPedido.PENDENTE);
        Long vendasPendentes = vendaRepository.countVendasPendentesPagamento();

        HomeKpisResponseDto response = new HomeKpisResponseDto();
        response.setTotalItensEstoque(totalItensEstoque != null ? totalItensEstoque : 0L);
        response.setPedidosPendentes(pedidosPendentes != null ? pedidosPendentes : 0);
        response.setVendasPendentes(vendasPendentes != null ? vendasPendentes : 0L);
        return response;
    }

    public List<FaturamentoItemDTO> getFaturamento(String periodo) {
        return switch (periodo) {
            case "7" -> {
                LocalDate dataInicio = LocalDate.now().minusDays(7);
                List<Object[]> rows = vendaRepository.findFaturamentoPorDiaSemana(dataInicio);
                Map<String, BigDecimal> byKey = rows.stream()
                        .collect(Collectors.toMap(r -> (String) r[0], r -> toBigDecimal(r[1])));
                String[] labels = {"Seg", "Ter", "Qua", "Qui", "Sex", "Sáb", "Dom"};
                List<FaturamentoItemDTO> result = new ArrayList<>();
                for (int i = 0; i < 7; i++) {
                    String key = String.valueOf(i + 1);
                    result.add(new FaturamentoItemDTO(labels[i], byKey.getOrDefault(key, BigDecimal.ZERO)));
                }
                yield result;
            }
            case "30" -> {
                LocalDate dataInicio = LocalDate.now().minusDays(30);
                List<Object[]> rows = vendaRepository.findFaturamentoPorSemanaMes(dataInicio);
                Map<String, BigDecimal> byKey = rows.stream()
                        .collect(Collectors.toMap(r -> (String) r[0], r -> toBigDecimal(r[1])));
                List<FaturamentoItemDTO> result = new ArrayList<>();
                for (int i = 1; i <= 4; i++) {
                    result.add(new FaturamentoItemDTO("S" + i, byKey.getOrDefault(String.valueOf(i), BigDecimal.ZERO)));
                }
                yield result;
            }
            case "90" -> {
                LocalDate dataInicio = LocalDate.now().minusDays(90);
                List<Object[]> rows = vendaRepository.findFaturamentoPorMes(dataInicio);
                Map<String, BigDecimal> byKey = rows.stream()
                        .collect(Collectors.toMap(r -> (String) r[0], r -> toBigDecimal(r[1])));
                String[] meses = {"Jan", "Fev", "Mar", "Abr", "Mai", "Jun",
                                  "Jul", "Ago", "Set", "Out", "Nov", "Dez"};
                List<FaturamentoItemDTO> result = new ArrayList<>();
                LocalDate cursor = dataInicio.withDayOfMonth(1);
                LocalDate hoje = LocalDate.now();
                while (!cursor.isAfter(hoje)) {
                    String key = String.format("%02d", cursor.getMonthValue());
                    result.add(new FaturamentoItemDTO(meses[cursor.getMonthValue() - 1], byKey.getOrDefault(key, BigDecimal.ZERO)));
                    cursor = cursor.plusMonths(1);
                }
                yield result;
            }
            default -> throw new IllegalArgumentException("Período inválido. Use 7, 30 ou 90.");
        };
    }

    public List<StatusPedidoDTO> getStatusPedidos() {
        List<Object[]> rows = pedidoVendaRepository.countGroupedByStatus();
        Map<StatusPedido, Long> counts = rows.stream()
                .collect(Collectors.toMap(r -> (StatusPedido) r[0], r -> (Long) r[1]));
        return List.of(
                new StatusPedidoDTO("Concluído", counts.getOrDefault(StatusPedido.CONCLUIDO, 0L)),
                new StatusPedidoDTO("Pendente",  counts.getOrDefault(StatusPedido.PENDENTE,  0L)),
                new StatusPedidoDTO("Cancelado", counts.getOrDefault(StatusPedido.CANCELADO, 0L))
        );
    }

    public List<PagamentoPendenteDTO> getPagamentosPendentes() {
        List<Object[]> rows = vendaRepository.findPagamentosPendentes(PageRequest.of(0, 10));
        return rows.stream().map(r -> {
            String cliente = r[0] + " " + r[1];
            String data = ((LocalDate) r[2]).format(DateTimeFormatter.ISO_LOCAL_DATE);
            BigDecimal valor = toBigDecimal(r[3]);
            return new PagamentoPendenteDTO(cliente.strip(), data, valor);
        }).collect(Collectors.toList());
    }

    public List<ProdutoEstoqueDTO> getProdutosEstoque() {
        List<Object[]> rows = produtoRepository.findProdutosComDemandaEEstoque();
        return rows.stream().map(r -> {
            String nome = (String) r[0];
            Long pedidos = r[1] instanceof Long l ? l : ((Number) r[1]).longValue();
            Integer estoque = r[2] instanceof Integer i ? i : ((Number) r[2]).intValue();
            return new ProdutoEstoqueDTO(nome, pedidos, estoque);
        }).collect(Collectors.toList());
    }

    public List<RankingCompradorDTO> getRankingCompradores(String periodo) {
        int dias = parsePeriodo(periodo);
        LocalDate dataInicio = LocalDate.now().minusDays(dias);
        List<Object[]> rows = vendaRepository.findRankingCompradores(dataInicio, PageRequest.of(0, 5));
        return rows.stream().map(r -> {
            String nome = (r[0] + " " + r[1]).strip();
            BigDecimal total = toBigDecimal(r[2]);
            return new RankingCompradorDTO(nome, total);
        }).collect(Collectors.toList());
    }

    public List<ClienteInativoDTO> getClientesInativos(String diasSemPedido) {
        int dias = switch (diasSemPedido) {
            case "30" -> 30;
            case "60" -> 60;
            default -> throw new IllegalArgumentException("diasSemPedido inválido. Use 30 ou 60.");
        };
        LocalDateTime corte = LocalDateTime.now().minusDays(dias);
        List<Object[]> rows = pedidoVendaRepository.findClientesInativos(corte, PageRequest.of(0, 10));
        LocalDate hoje = LocalDate.now();
        return rows.stream().map(r -> {
            String nome = (r[0] + " " + r[1]).strip();
            LocalDateTime ultimoPedidoDt = (LocalDateTime) r[2];
            String ultimoPedido = ultimoPedidoDt.toLocalDate().format(DateTimeFormatter.ISO_LOCAL_DATE);
            long diffDias = ChronoUnit.DAYS.between(ultimoPedidoDt.toLocalDate(), hoje);
            return new ClienteInativoDTO(nome, ultimoPedido, diffDias);
        }).collect(Collectors.toList());
    }

    private int parsePeriodo(String periodo) {
        return switch (periodo) {
            case "7"  -> 7;
            case "30" -> 30;
            case "90" -> 90;
            default   -> throw new IllegalArgumentException("Período inválido. Use 7, 30 ou 90.");
        };
    }

    private BigDecimal toBigDecimal(Object value) {
        if (value == null) return BigDecimal.ZERO;
        if (value instanceof BigDecimal bd) return bd;
        return new BigDecimal(value.toString());
    }
}
