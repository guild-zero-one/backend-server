package com.zeroone.simlady.service;

import com.zeroone.simlady.dto.dashboard.HomeKpisResponseDto;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DashboardServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private PedidoVendaRepository pedidoVendaRepository;

    @Mock
    private VendaRepository vendaRepository;

    @InjectMocks
    private DashboardService dashboardService;

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
}
