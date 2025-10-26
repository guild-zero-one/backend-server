package com.zeroone.simlady.core.application.usecases.venda;

import com.zeroone.simlady.core.application.ports.VendaRepositoryPort;
import com.zeroone.simlady.core.domain.venda.Venda;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListarVendasUseCaseTest {

    @Mock
    private VendaRepositoryPort repository;

    @InjectMocks
    private ListarVendasUseCase listarVendasUseCase;
    
    @BeforeEach
    void setUp() {
        reset(repository);
    }

    @Test
    @DisplayName("Deve listar vendas com sucesso")
    void deveListarVendasComSucesso() {
        // Given
        int pagina = 0;
        int tamanho = 10;
        
        UUID vendaId1 = UUID.randomUUID();
        Venda venda1 = Venda.of(
                vendaId1,
                com.zeroone.simlady.core.domain.venda.vendaVOs.ValorTotal.of("1000.0"),
                com.zeroone.simlady.core.domain.venda.vendaVOs.Desconto.of("100.0"),
                false,
                LocalDate.now(),
                new java.util.ArrayList<>(),
                java.time.LocalDateTime.now(),
                java.time.LocalDateTime.now()
        );

        UUID vendaId2 = UUID.randomUUID();
        Venda venda2 = Venda.of(
                vendaId2,
                com.zeroone.simlady.core.domain.venda.vendaVOs.ValorTotal.of("2000.0"),
                com.zeroone.simlady.core.domain.venda.vendaVOs.Desconto.of("200.0"),
                false,
                LocalDate.now(),
                new java.util.ArrayList<>(),
                java.time.LocalDateTime.now(),
                java.time.LocalDateTime.now()
        );

        List<Venda> vendas = List.of(venda1, venda2);
        Page<Venda> page = new PageImpl<>(vendas, PageRequest.of(pagina, tamanho), 2);

        when(repository.listarTodas(eq(pagina), eq(tamanho))).thenReturn(page);

        // When
        Page<Venda> resultado = listarVendasUseCase.executar(pagina, tamanho);

        // Then
        assertNotNull(resultado);
        assertEquals(2, resultado.getTotalElements());
        assertEquals(2, resultado.getContent().size());
        assertEquals(1000.0, resultado.getContent().get(0).getValorTotal().getValor().doubleValue());
        assertEquals(2000.0, resultado.getContent().get(1).getValorTotal().getValor().doubleValue());
        verify(repository).listarTodas(pagina, tamanho);
    }

    @Test
    @DisplayName("Deve retornar página vazia quando não há vendas")
    void deveRetornarPaginaVaziaQuandoNaoHaVendas() {
        // Given
        int pagina = 0;
        int tamanho = 10;
        Page<Venda> pageVazia = new PageImpl<>(List.of(), PageRequest.of(pagina, tamanho), 0);

        when(repository.listarTodas(pagina, tamanho)).thenReturn(pageVazia);

        // When
        Page<Venda> resultado = listarVendasUseCase.executar(pagina, tamanho);

        // Then
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        assertEquals(0, resultado.getTotalElements());
        verify(repository).listarTodas(pagina, tamanho);
    }

    @Test
    @DisplayName("Deve listar vendas com paginação")
    void deveListarVendasComPaginacao() {
        // Given
        int pagina = 1;
        int tamanho = 5;
        
        UUID vendaId = UUID.randomUUID();
        Venda venda = Venda.of(
                vendaId,
                com.zeroone.simlady.core.domain.venda.vendaVOs.ValorTotal.of("1500.0"),
                com.zeroone.simlady.core.domain.venda.vendaVOs.Desconto.of("150.0"),
                false,
                LocalDate.now(),
                new java.util.ArrayList<>(),
                java.time.LocalDateTime.now(),
                java.time.LocalDateTime.now()
        );

        List<Venda> vendas = List.of(venda);
        Page<Venda> page = new PageImpl<>(vendas, PageRequest.of(pagina, tamanho), 1);

        when(repository.listarTodas(eq(pagina), eq(tamanho))).thenReturn(page);

        // When
        Page<Venda> resultado = listarVendasUseCase.executar(pagina, tamanho);

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.getContent().size());
        assertEquals(pagina, resultado.getNumber());
        assertEquals(tamanho, resultado.getSize());
        assertEquals(1500.0, resultado.getContent().get(0).getValorTotal().getValor().doubleValue());
        verify(repository).listarTodas(pagina, tamanho);
    }

    @Test
    @DisplayName("Deve listar vendas com diferentes valores")
    void deveListarVendasComDiferentesValores() {
        // Given
        int pagina = 0;
        int tamanho = 10;
        
        UUID vendaComDescontoId = UUID.randomUUID();
        Venda vendaComDesconto = Venda.of(
                vendaComDescontoId,
                com.zeroone.simlady.core.domain.venda.vendaVOs.ValorTotal.of("1000.0"),
                com.zeroone.simlady.core.domain.venda.vendaVOs.Desconto.of("100.0"),
                false,
                LocalDate.now(),
                new java.util.ArrayList<>(),
                java.time.LocalDateTime.now(),
                java.time.LocalDateTime.now()
        );

        UUID vendaSemDescontoId = UUID.randomUUID();
        Venda vendaSemDesconto = Venda.of(
                vendaSemDescontoId,
                com.zeroone.simlady.core.domain.venda.vendaVOs.ValorTotal.of("500.0"),
                com.zeroone.simlady.core.domain.venda.vendaVOs.Desconto.of("0.0"),
                false,
                LocalDate.now(),
                new java.util.ArrayList<>(),
                java.time.LocalDateTime.now(),
                java.time.LocalDateTime.now()
        );

        List<Venda> vendas = List.of(vendaComDesconto, vendaSemDesconto);
        Page<Venda> page = new PageImpl<>(vendas, PageRequest.of(pagina, tamanho), 2);

        when(repository.listarTodas(eq(pagina), eq(tamanho))).thenReturn(page);

        // When
        Page<Venda> resultado = listarVendasUseCase.executar(pagina, tamanho);

        // Then
        assertNotNull(resultado);
        assertEquals(2, resultado.getTotalElements());
        assertEquals(1000.0, resultado.getContent().get(0).getValorTotal().getValor().doubleValue());
        assertEquals(100.0, resultado.getContent().get(0).getDesconto().getValor().doubleValue());
        assertEquals(500.0, resultado.getContent().get(1).getValorTotal().getValor().doubleValue());
        assertEquals(0.0, resultado.getContent().get(1).getDesconto().getValor().doubleValue());
        verify(repository).listarTodas(pagina, tamanho);
    }
}
