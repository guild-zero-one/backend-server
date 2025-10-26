package com.zeroone.simlady.core.application.usecases.venda;

import com.zeroone.simlady.core.application.ports.VendaRepositoryPort;
import com.zeroone.simlady.core.domain.venda.Venda;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BuscarVendaPorIdUseCaseTest {

    @Mock
    private VendaRepositoryPort repository;

    @InjectMocks
    private BuscarVendaPorIdUseCase buscarVendaPorIdUseCase;

    @Test
    @DisplayName("Deve buscar venda por ID com sucesso")
    void deveBuscarVendaPorIdComSucesso() {
        // Given
        UUID id = UUID.randomUUID();
        Venda venda = Venda.of(
                id,
                com.zeroone.simlady.core.domain.venda.vendaVOs.ValorTotal.of("1000.0"),
                com.zeroone.simlady.core.domain.venda.vendaVOs.Desconto.of("100.0"),
                false,
                LocalDate.now(),
                new java.util.ArrayList<>(),
                java.time.LocalDateTime.now(),
                java.time.LocalDateTime.now()
        );

        when(repository.buscarPorId(id)).thenReturn(Optional.of(venda));

        // When
        Optional<Venda> resultado = buscarVendaPorIdUseCase.executar(id);

        // Then
        assertTrue(resultado.isPresent());
        assertEquals(id, resultado.get().getId());
        assertEquals(1000.0, resultado.get().getValorTotal().getValor().doubleValue());
        assertEquals(100.0, resultado.get().getDesconto().getValor().doubleValue());
        verify(repository).buscarPorId(id);
    }

    @Test
    @DisplayName("Deve retornar Optional vazio quando venda não for encontrada")
    void deveRetornarOptionalVazioQuandoVendaNaoForEncontrada() {
        // Given
        UUID id = UUID.randomUUID();
        when(repository.buscarPorId(id)).thenReturn(Optional.empty());

        // When
        Optional<Venda> resultado = buscarVendaPorIdUseCase.executar(id);

        // Then
        assertTrue(resultado.isEmpty());
        verify(repository).buscarPorId(id);
    }

    @Test
    @DisplayName("Deve buscar venda com pedidos")
    void deveBuscarVendaComPedidos() {
        // Given
        UUID id = UUID.randomUUID();
        UUID pedidoId1 = UUID.randomUUID();
        UUID pedidoId2 = UUID.randomUUID();
        
        Venda venda = Venda.of(
                id,
                com.zeroone.simlady.core.domain.venda.vendaVOs.ValorTotal.of("2000.0"),
                com.zeroone.simlady.core.domain.venda.vendaVOs.Desconto.of("200.0"),
                false,
                LocalDate.now(),
                new java.util.ArrayList<>(),
                java.time.LocalDateTime.now(),
                java.time.LocalDateTime.now()
        );
        venda.adicionarPedido(pedidoId1);
        venda.adicionarPedido(pedidoId2);

        when(repository.buscarPorId(id)).thenReturn(Optional.of(venda));

        // When
        Optional<Venda> resultado = buscarVendaPorIdUseCase.executar(id);

        // Then
        assertTrue(resultado.isPresent());
        assertEquals(id, resultado.get().getId());
        assertEquals(2, resultado.get().getPedidosIds().size());
        assertTrue(resultado.get().getPedidosIds().contains(pedidoId1));
        assertTrue(resultado.get().getPedidosIds().contains(pedidoId2));
        verify(repository).buscarPorId(id);
    }

    @Test
    @DisplayName("Deve buscar venda sem pedidos")
    void deveBuscarVendaSemPedidos() {
        // Given
        UUID id = UUID.randomUUID();
        Venda venda = Venda.of(
                id,
                com.zeroone.simlady.core.domain.venda.vendaVOs.ValorTotal.of("500.0"),
                com.zeroone.simlady.core.domain.venda.vendaVOs.Desconto.of("0.0"),
                false,
                LocalDate.now(),
                new java.util.ArrayList<>(),
                java.time.LocalDateTime.now(),
                java.time.LocalDateTime.now()
        );

        when(repository.buscarPorId(id)).thenReturn(Optional.of(venda));

        // When
        Optional<Venda> resultado = buscarVendaPorIdUseCase.executar(id);

        // Then
        assertTrue(resultado.isPresent());
        assertEquals(id, resultado.get().getId());
        assertTrue(resultado.get().getPedidosIds().isEmpty());
        verify(repository).buscarPorId(id);
    }
}
