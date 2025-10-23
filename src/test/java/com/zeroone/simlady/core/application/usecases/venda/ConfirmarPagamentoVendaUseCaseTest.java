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
class ConfirmarPagamentoVendaUseCaseTest {

    @Mock
    private VendaRepositoryPort repository;

    @InjectMocks
    private ConfirmarPagamentoVendaUseCase confirmarPagamentoVendaUseCase;

    @Test
    @DisplayName("Deve confirmar pagamento da venda com sucesso")
    void deveConfirmarPagamentoDaVendaComSucesso() {
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
        
        Venda vendaComPagamentoConfirmado = Venda.of(
                id,
                com.zeroone.simlady.core.domain.venda.vendaVOs.ValorTotal.of("1000.0"),
                com.zeroone.simlady.core.domain.venda.vendaVOs.Desconto.of("100.0"),
                true,
                LocalDate.now(),
                new java.util.ArrayList<>(),
                java.time.LocalDateTime.now(),
                java.time.LocalDateTime.now()
        );

        when(repository.buscarPorId(id)).thenReturn(Optional.of(venda));
        when(repository.atualizarVenda(any(Venda.class))).thenReturn(vendaComPagamentoConfirmado);

        // When
        Optional<Venda> resultado = confirmarPagamentoVendaUseCase.executar(id);

        // Then
        assertTrue(resultado.isPresent());
        assertEquals(id, resultado.get().getId());
        assertTrue(resultado.get().getPagamentoRealizado());
        verify(repository).buscarPorId(id);
        verify(repository).atualizarVenda(any(Venda.class));
    }

    @Test
    @DisplayName("Deve retornar Optional vazio quando venda não for encontrada")
    void deveRetornarOptionalVazioQuandoVendaNaoForEncontrada() {
        // Given
        UUID id = UUID.randomUUID();
        when(repository.buscarPorId(id)).thenReturn(Optional.empty());

        // When
        Optional<Venda> resultado = confirmarPagamentoVendaUseCase.executar(id);

        // Then
        assertTrue(resultado.isEmpty());
        verify(repository).buscarPorId(id);
        verify(repository, never()).atualizarVenda(any(Venda.class));
    }

    @Test
    @DisplayName("Deve confirmar pagamento de venda já paga")
    void deveConfirmarPagamentoDeVendaJaPaga() {
        // Given
        UUID id = UUID.randomUUID();
        Venda venda = Venda.of(
                id,
                com.zeroone.simlady.core.domain.venda.vendaVOs.ValorTotal.of("500.0"),
                com.zeroone.simlady.core.domain.venda.vendaVOs.Desconto.of("0.0"),
                true,
                LocalDate.now(),
                new java.util.ArrayList<>(),
                java.time.LocalDateTime.now(),
                java.time.LocalDateTime.now()
        );

        when(repository.buscarPorId(id)).thenReturn(Optional.of(venda));
        when(repository.atualizarVenda(any(Venda.class))).thenReturn(venda);

        // When
        Optional<Venda> resultado = confirmarPagamentoVendaUseCase.executar(id);

        // Then
        assertTrue(resultado.isPresent());
        assertEquals(id, resultado.get().getId());
        assertTrue(resultado.get().getPagamentoRealizado());
        verify(repository).buscarPorId(id);
        verify(repository).atualizarVenda(any(Venda.class));
    }

    @Test
    @DisplayName("Deve confirmar pagamento de venda com pedidos")
    void deveConfirmarPagamentoDeVendaComPedidos() {
        // Given
        UUID id = UUID.randomUUID();
        UUID pedidoId = UUID.randomUUID();
        
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
        venda.adicionarPedido(pedidoId);
        
        Venda vendaComPagamentoConfirmado = Venda.of(
                id,
                com.zeroone.simlady.core.domain.venda.vendaVOs.ValorTotal.of("2000.0"),
                com.zeroone.simlady.core.domain.venda.vendaVOs.Desconto.of("200.0"),
                true,
                LocalDate.now(),
                new java.util.ArrayList<>(),
                java.time.LocalDateTime.now(),
                java.time.LocalDateTime.now()
        );
        vendaComPagamentoConfirmado.adicionarPedido(pedidoId);

        when(repository.buscarPorId(id)).thenReturn(Optional.of(venda));
        when(repository.atualizarVenda(any(Venda.class))).thenReturn(vendaComPagamentoConfirmado);

        // When
        Optional<Venda> resultado = confirmarPagamentoVendaUseCase.executar(id);

        // Then
        assertTrue(resultado.isPresent());
        assertEquals(id, resultado.get().getId());
        assertTrue(resultado.get().getPagamentoRealizado());
        assertEquals(1, resultado.get().getPedidosIds().size());
        assertTrue(resultado.get().getPedidosIds().contains(pedidoId));
        verify(repository).buscarPorId(id);
        verify(repository).atualizarVenda(any(Venda.class));
    }
}
