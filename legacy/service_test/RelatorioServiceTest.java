package com.zeroone.simlady.service_test;

import com.zeroone.simlady.dto.produto.ProdutosMaisVendidosResponseDto;
import com.zeroone.simlady.repository.PedidoItemRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RelatorioServiceTest {

    @Mock
    private PedidoItemRepository pedidoItemRepository;

    @InjectMocks
    private RelatorioService relatorioService;

    @Test
    @DisplayName("Deve listar vendas por produto com sucesso")
    void deveListarVendasPorProdutoComSucesso() {
        // Given
        ProdutosMaisVendidosResponseDto produto1 = new ProdutosMaisVendidosResponseDto(
                1,
                "Produto A",
                10L,
                new BigDecimal("1000.00")
        );

        ProdutosMaisVendidosResponseDto produto2 = new ProdutosMaisVendidosResponseDto(
                2,
                "Produto B",
                5L,
                new BigDecimal("500.00")
        );

        when(pedidoItemRepository.buscarProdutosMaisVendidos())
                .thenReturn(List.of(produto1, produto2));

        // When
        List<ProdutosMaisVendidosResponseDto> resultado = relatorioService.listarVendasPorProduto();

        // Then
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Produto A", resultado.getFirst().getNomeProduto());
        assertEquals(10L, resultado.getFirst().getTotalVendido());
        assertEquals(new BigDecimal("1000.00"), resultado.getFirst().getValorTotalVendido());
        verify(pedidoItemRepository).buscarProdutosMaisVendidos();
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não houver vendas")
    void deveRetornarListaVaziaQuandoNaoHouverVendas() {
        // Given
        when(pedidoItemRepository.buscarProdutosMaisVendidos())
                .thenReturn(List.of());

        // When
        List<ProdutosMaisVendidosResponseDto> resultado = relatorioService.listarVendasPorProduto();

        // Then
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(pedidoItemRepository).buscarProdutosMaisVendidos();
    }

    @Test
    @DisplayName("Deve ordenar produtos por quantidade vendida em ordem decrescente")
    void deveOrdenarProdutosPorQuantidadeVendidaEmOrdemDecrescente() {
        // Given
        ProdutosMaisVendidosResponseDto produtoMaisVendido = new ProdutosMaisVendidosResponseDto(
                1,
                "Produto Popular",
                100L,
                new BigDecimal("10000.00")
        );

        ProdutosMaisVendidosResponseDto produtoMenosVendido = new ProdutosMaisVendidosResponseDto(
                2,
                "Produto Menos Popular",
                50L,
                new BigDecimal("5000.00")
        );

        when(pedidoItemRepository.buscarProdutosMaisVendidos())
                .thenReturn(List.of(produtoMaisVendido, produtoMenosVendido));

        // When
        List<ProdutosMaisVendidosResponseDto> resultado = relatorioService.listarVendasPorProduto();

        // Then
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(100L, resultado.get(0).getTotalVendido());
        assertEquals(50L, resultado.get(1).getTotalVendido());
        assertTrue(resultado.get(0).getTotalVendido() > resultado.get(1).getTotalVendido());
        verify(pedidoItemRepository).buscarProdutosMaisVendidos();
    }

    @Test
    @DisplayName("Deve calcular valores totais corretamente")
    void deveCalcularValoresTotaisCorretamente() {
        // Given
        ProdutosMaisVendidosResponseDto produto = new ProdutosMaisVendidosResponseDto(
                1,
                "Produto Test",
                10L,
                new BigDecimal("1000.00")
        );

        when(pedidoItemRepository.buscarProdutosMaisVendidos())
                .thenReturn(List.of(produto));

        // When
        List<ProdutosMaisVendidosResponseDto> resultado = relatorioService.listarVendasPorProduto();

        // Then
        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        assertEquals(new BigDecimal("1000.00"), resultado.getFirst().getValorTotalVendido());
        assertEquals(10L, resultado.getFirst().getTotalVendido());
        verify(pedidoItemRepository).buscarProdutosMaisVendidos();
    }
}