package com.zeroone.simlady.service;

import com.zeroone.simlady.dto.dashboard.*;
import com.zeroone.simlady.entity.enums.StatusPedido;
import com.zeroone.simlady.repository.PedidoVendaRepository;
import com.zeroone.simlady.repository.ProdutoRepository;
import com.zeroone.simlady.repository.VendaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DashboardServiceTest {

    @Mock private ProdutoRepository produtoRepository;
    @Mock private PedidoVendaRepository pedidoVendaRepository;
    @Mock private VendaRepository vendaRepository;

    @InjectMocks
    private DashboardService dashboardService;

    // ---- obterHomeKpis ----

    @Test
    @DisplayName("Deve retornar KPIs da home com os valores agregados")
    void deveRetornarHomeKpisComValoresAgregados() {
        when(produtoRepository.sumQuantidadeTotalEmEstoque()).thenReturn(512L);
        when(pedidoVendaRepository.countByStatus(StatusPedido.PENDENTE)).thenReturn(20);
        when(vendaRepository.countVendasPendentesPagamento()).thenReturn(7L);

        HomeKpisResponseDto response = dashboardService.obterHomeKpis();

        assertEquals(512L, response.getTotalItensEstoque());
        assertEquals(20, response.getPedidosPendentes());
        assertEquals(7L, response.getVendasPendentes());
        verify(produtoRepository).sumQuantidadeTotalEmEstoque();
        verify(pedidoVendaRepository).countByStatus(StatusPedido.PENDENTE);
        verify(vendaRepository).countVendasPendentesPagamento();
    }

    @Test
    @DisplayName("Deve retornar zero quando agregações vierem nulas")
    void deveRetornarZeroQuandoAgregacoesVieremNulas() {
        when(produtoRepository.sumQuantidadeTotalEmEstoque()).thenReturn(null);
        when(pedidoVendaRepository.countByStatus(StatusPedido.PENDENTE)).thenReturn(null);
        when(vendaRepository.countVendasPendentesPagamento()).thenReturn(null);

        HomeKpisResponseDto response = dashboardService.obterHomeKpis();

        assertEquals(0L, response.getTotalItensEstoque());
        assertEquals(0, response.getPedidosPendentes());
        assertEquals(0L, response.getVendasPendentes());
    }

    // ---- getFaturamento ----

    @Test
    @DisplayName("periodo=7 deve retornar sempre 7 itens com labels de dia da semana")
    void getFaturamento_periodo7_deveRetornarSeteDias() {
        List<Object[]> rows = List.of(
                new Object[]{"1", new BigDecimal("200.00")},
                new Object[]{"3", new BigDecimal("350.00")}
        );
        when(vendaRepository.findFaturamentoPorDiaSemana(any(LocalDate.class))).thenReturn(rows);

        List<FaturamentoItemDTO> result = dashboardService.getFaturamento("7");

        assertThat(result).hasSize(7);
        assertThat(result.get(0).label()).isEqualTo("Seg");
        assertThat(result.get(0).valor()).isEqualByComparingTo(new BigDecimal("200.00"));
        assertThat(result.get(1).label()).isEqualTo("Ter");
        assertThat(result.get(1).valor()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(result.get(2).label()).isEqualTo("Qua");
        assertThat(result.get(2).valor()).isEqualByComparingTo(new BigDecimal("350.00"));
        verify(vendaRepository, times(1)).findFaturamentoPorDiaSemana(any(LocalDate.class));
    }

    @Test
    @DisplayName("periodo=30 deve retornar sempre 4 itens com labels S1..S4")
    void getFaturamento_periodo30_deveRetornarQuatroSemanas() {
        List<Object[]> rows = List.of(
                new Object[]{"1", new BigDecimal("420.00")},
                new Object[]{"2", new BigDecimal("680.00")}
        );
        when(vendaRepository.findFaturamentoPorSemanaMes(any(LocalDate.class))).thenReturn(rows);

        List<FaturamentoItemDTO> result = dashboardService.getFaturamento("30");

        assertThat(result).hasSize(4);
        assertThat(result.get(0).label()).isEqualTo("S1");
        assertThat(result.get(0).valor()).isEqualByComparingTo(new BigDecimal("420.00"));
        assertThat(result.get(1).label()).isEqualTo("S2");
        assertThat(result.get(1).valor()).isEqualByComparingTo(new BigDecimal("680.00"));
        assertThat(result.get(2).label()).isEqualTo("S3");
        assertThat(result.get(2).valor()).isEqualByComparingTo(BigDecimal.ZERO);
        verify(vendaRepository, times(1)).findFaturamentoPorSemanaMes(any(LocalDate.class));
    }

    @Test
    @DisplayName("periodo=90 deve retornar os meses do intervalo com valores do repositório")
    void getFaturamento_periodo90_deveRetornarMesesDoIntervalo() {
        LocalDate hoje = LocalDate.now();
        int mesAtual = hoje.getMonthValue();
        String keyMesAtual = String.format("%02d", mesAtual);
        List<Object[]> rows = List.<Object[]>of(new Object[]{keyMesAtual, new BigDecimal("900.00")});
        when(vendaRepository.findFaturamentoPorMes(any(LocalDate.class))).thenReturn(rows);

        List<FaturamentoItemDTO> result = dashboardService.getFaturamento("90");

        assertThat(result).isNotEmpty();
        assertThat(result).anyMatch(item -> item.valor().compareTo(new BigDecimal("900.00")) == 0);
        verify(vendaRepository, times(1)).findFaturamentoPorMes(any(LocalDate.class));
    }

    @Test
    @DisplayName("Repositório vazio deve retornar lista com zeros sem lançar exceção")
    void getFaturamento_repositorioVazio_naoDeveLancarExcecao() {
        when(vendaRepository.findFaturamentoPorDiaSemana(any(LocalDate.class))).thenReturn(List.of());

        List<FaturamentoItemDTO> result = dashboardService.getFaturamento("7");

        assertThat(result).hasSize(7);
        assertThat(result).allMatch(item -> item.valor().compareTo(BigDecimal.ZERO) == 0);
    }

    @Test
    @DisplayName("periodo inválido deve lançar IllegalArgumentException")
    void getFaturamento_periodoInvalido_deveLancarExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> dashboardService.getFaturamento("365"));
    }

    // ---- getStatusPedidos ----

    @Test
    @DisplayName("Deve retornar 3 itens na ordem Concluído → Pendente → Cancelado")
    void getStatusPedidos_deveRetornarTresItensOrdenados() {
        List<Object[]> rows = List.of(
                new Object[]{StatusPedido.CONCLUIDO, 42L},
                new Object[]{StatusPedido.PENDENTE,  15L}
        );
        when(pedidoVendaRepository.countGroupedByStatus()).thenReturn(rows);

        List<StatusPedidoDTO> result = dashboardService.getStatusPedidos();

        assertThat(result).hasSize(3);
        assertThat(result.get(0).label()).isEqualTo("Concluído");
        assertThat(result.get(0).value()).isEqualTo(42L);
        assertThat(result.get(1).label()).isEqualTo("Pendente");
        assertThat(result.get(1).value()).isEqualTo(15L);
        assertThat(result.get(2).label()).isEqualTo("Cancelado");
        assertThat(result.get(2).value()).isEqualTo(0L);
    }

    @Test
    @DisplayName("Repositório vazio deve retornar 3 itens com value=0")
    void getStatusPedidos_repositorioVazio_deveRetornarTresItensComZero() {
        when(pedidoVendaRepository.countGroupedByStatus()).thenReturn(List.of());

        List<StatusPedidoDTO> result = dashboardService.getStatusPedidos();

        assertThat(result).hasSize(3);
        assertThat(result).allMatch(dto -> dto.value() == 0L);
    }

    // ---- getPagamentosPendentes ----

    @Test
    @DisplayName("Deve mapear nome+sobrenome e formatar data corretamente")
    void getPagamentosPendentes_deveMaperarClienteEData() {
        List<Object[]> rows = List.<Object[]>of(
                new Object[]{"Ana", "Lima", LocalDate.of(2026, 5, 25), new BigDecimal("157.50")}
        );
        when(vendaRepository.findPagamentosPendentes(any())).thenReturn(rows);

        List<PagamentoPendenteDTO> result = dashboardService.getPagamentosPendentes();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).cliente()).isEqualTo("Ana Lima");
        assertThat(result.get(0).data()).isEqualTo("2026-05-25");
        assertThat(result.get(0).valor()).isEqualByComparingTo(new BigDecimal("157.50"));
    }

    @Test
    @DisplayName("Repositório vazio deve retornar lista vazia")
    void getPagamentosPendentes_repositorioVazio_deveRetornarListaVazia() {
        when(vendaRepository.findPagamentosPendentes(any())).thenReturn(List.of());

        assertThat(dashboardService.getPagamentosPendentes()).isEmpty();
    }

    // ---- getProdutosEstoque ----

    @Test
    @DisplayName("Deve mapear campos do produto corretamente")
    void getProdutosEstoque_deveMaperarCampos() {
        List<Object[]> rows = List.<Object[]>of(
                new Object[]{"Sérum Antiqueda 60ml", 27L, 15}
        );
        when(produtoRepository.findProdutosComDemandaEEstoque()).thenReturn(rows);

        List<ProdutoEstoqueDTO> result = dashboardService.getProdutosEstoque();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).nome()).isEqualTo("Sérum Antiqueda 60ml");
        assertThat(result.get(0).pedidos()).isEqualTo(27L);
        assertThat(result.get(0).estoque()).isEqualTo(15);
    }

    @Test
    @DisplayName("Repositório vazio deve retornar lista vazia")
    void getProdutosEstoque_repositorioVazio_deveRetornarListaVazia() {
        when(produtoRepository.findProdutosComDemandaEEstoque()).thenReturn(List.of());

        assertThat(dashboardService.getProdutosEstoque()).isEmpty();
    }

    // ---- getRankingCompradores ----

    @Test
    @DisplayName("periodo=30 deve mapear nome completo e total corretamente")
    void getRankingCompradores_periodo30_deveMaperarCampos() {
        List<Object[]> rows = List.<Object[]>of(
                new Object[]{"Ana", "Lima", new BigDecimal("1250.50")},
                new Object[]{"Maria", "Souza", new BigDecimal("980.00")}
        );
        when(vendaRepository.findRankingCompradores(any(LocalDate.class), any())).thenReturn(rows);

        List<RankingCompradorDTO> result = dashboardService.getRankingCompradores("30");

        assertThat(result).hasSize(2);
        assertThat(result.get(0).nome()).isEqualTo("Ana Lima");
        assertThat(result.get(0).total()).isEqualByComparingTo(new BigDecimal("1250.50"));
        assertThat(result.get(1).nome()).isEqualTo("Maria Souza");
    }

    @Test
    @DisplayName("Repositório vazio deve retornar lista vazia")
    void getRankingCompradores_repositorioVazio_deveRetornarListaVazia() {
        when(vendaRepository.findRankingCompradores(any(LocalDate.class), any())).thenReturn(List.of());

        assertThat(dashboardService.getRankingCompradores("7")).isEmpty();
    }

    @Test
    @DisplayName("periodo inválido deve lançar IllegalArgumentException")
    void getRankingCompradores_periodoInvalido_deveLancarExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> dashboardService.getRankingCompradores("365"));
    }

    // ---- getClientesInativos ----

    @Test
    @DisplayName("diasSemPedido=30 deve mapear nome, data e calcular dias corretamente")
    void getClientesInativos_dias30_deveMaperarCampos() {
        LocalDateTime ultimoPedido = LocalDateTime.of(2026, 4, 28, 10, 0);
        List<Object[]> rows = List.<Object[]>of(
                new Object[]{"Beatriz", "Nunes", ultimoPedido}
        );
        when(pedidoVendaRepository.findClientesInativos(any(LocalDateTime.class), any())).thenReturn(rows);

        List<ClienteInativoDTO> result = dashboardService.getClientesInativos("30");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).nome()).isEqualTo("Beatriz Nunes");
        assertThat(result.get(0).ultimoPedido()).isEqualTo("2026-04-28");
        assertThat(result.get(0).dias()).isGreaterThan(0L);
    }

    @Test
    @DisplayName("diasSemPedido=60 deve chamar repositório com corte de 60 dias")
    void getClientesInativos_dias60_deveChamarRepositorioComCorteCorreto() {
        when(pedidoVendaRepository.findClientesInativos(any(LocalDateTime.class), any())).thenReturn(List.of());

        dashboardService.getClientesInativos("60");

        verify(pedidoVendaRepository, times(1)).findClientesInativos(any(LocalDateTime.class), any());
    }

    @Test
    @DisplayName("Repositório vazio deve retornar lista vazia")
    void getClientesInativos_repositorioVazio_deveRetornarListaVazia() {
        when(pedidoVendaRepository.findClientesInativos(any(LocalDateTime.class), any())).thenReturn(List.of());

        assertThat(dashboardService.getClientesInativos("30")).isEmpty();
    }

    @Test
    @DisplayName("diasSemPedido inválido deve lançar IllegalArgumentException")
    void getClientesInativos_diasInvalido_deveLancarExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> dashboardService.getClientesInativos("15"));
    }
}
