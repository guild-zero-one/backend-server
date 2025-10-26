package com.zeroone.simlady.core.application.usecases.venda;

import com.zeroone.simlady.core.application.ports.VendaRepositoryPort;
import com.zeroone.simlady.core.application.usecases.pedido.AlterarStatusPedidoUseCase;
import com.zeroone.simlady.core.domain.pedido.StatusPedido;
import com.zeroone.simlady.core.domain.venda.Venda;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CriarVendaUseCaseTest {

    @Mock
    private VendaRepositoryPort repository;

    @Mock
    private AlterarStatusPedidoUseCase alterarStatusPedidoUseCase;

    @InjectMocks
    private CriarVendaUseCase criarVendaUseCase;

    @Test
    @DisplayName("Deve criar venda com sucesso")
    void deveCriarVendaComSucesso() {
        // Given
        String valorTotal = "1000.0";
        String desconto = "100.0";
        LocalDate dataVenda = LocalDate.now();
        List<UUID> pedidosIds = List.of(UUID.randomUUID(), UUID.randomUUID());
        
        UUID vendaId = UUID.randomUUID();
        Venda vendaSalva = Venda.of(
                vendaId,
                com.zeroone.simlady.core.domain.venda.vendaVOs.ValorTotal.of(valorTotal),
                com.zeroone.simlady.core.domain.venda.vendaVOs.Desconto.of(desconto),
                false,
                dataVenda,
                new java.util.ArrayList<>(),
                java.time.LocalDateTime.now(),
                java.time.LocalDateTime.now()
        );

        when(repository.salvarVenda(any(Venda.class))).thenReturn(vendaSalva);

        // When
        Venda resultado = criarVendaUseCase.executar(valorTotal, desconto, dataVenda, pedidosIds);

        // Then
        assertNotNull(resultado);
        assertEquals(1000.0, resultado.getValorTotal().getValor().doubleValue());
        assertEquals(100.0, resultado.getDesconto().getValor().doubleValue());
        assertEquals(dataVenda, resultado.getDataVenda());
        verify(repository).salvarVenda(any(Venda.class));
        verify(alterarStatusPedidoUseCase, times(2)).executar(any(UUID.class), eq(StatusPedido.CONCLUIDO));
    }

    @Test
    @DisplayName("Deve criar venda sem pedidos")
    void deveCriarVendaSemPedidos() {
        // Given
        String valorTotal = "500.0";
        String desconto = "0.0";
        LocalDate dataVenda = LocalDate.now();
        
        UUID vendaId = UUID.randomUUID();
        Venda vendaSalva = Venda.of(
                vendaId,
                com.zeroone.simlady.core.domain.venda.vendaVOs.ValorTotal.of(valorTotal),
                com.zeroone.simlady.core.domain.venda.vendaVOs.Desconto.of(desconto),
                false,
                dataVenda,
                new java.util.ArrayList<>(),
                java.time.LocalDateTime.now(),
                java.time.LocalDateTime.now()
        );

        when(repository.salvarVenda(any(Venda.class))).thenReturn(vendaSalva);

        // When
        Venda resultado = criarVendaUseCase.executar(valorTotal, desconto, dataVenda, null);

        // Then
        assertNotNull(resultado);
        assertEquals(500.0, resultado.getValorTotal().getValor().doubleValue());
        assertEquals(0.0, resultado.getDesconto().getValor().doubleValue());
        assertEquals(dataVenda, resultado.getDataVenda());
        verify(repository).salvarVenda(any(Venda.class));
        verify(alterarStatusPedidoUseCase, never()).executar(any(UUID.class), any(StatusPedido.class));
    }

    @Test
    @DisplayName("Deve criar venda com múltiplos pedidos")
    void deveCriarVendaComMultiplosPedidos() {
        // Given
        String valorTotal = "2000.0";
        String desconto = "200.0";
        LocalDate dataVenda = LocalDate.now();
        List<UUID> pedidosIds = List.of(
                UUID.randomUUID(), 
                UUID.randomUUID(), 
                UUID.randomUUID()
        );
        
        UUID vendaId = UUID.randomUUID();
        Venda vendaSalva = Venda.of(
                vendaId,
                com.zeroone.simlady.core.domain.venda.vendaVOs.ValorTotal.of(valorTotal),
                com.zeroone.simlady.core.domain.venda.vendaVOs.Desconto.of(desconto),
                false,
                dataVenda,
                new java.util.ArrayList<>(),
                java.time.LocalDateTime.now(),
                java.time.LocalDateTime.now()
        );

        when(repository.salvarVenda(any(Venda.class))).thenReturn(vendaSalva);

        // When
        Venda resultado = criarVendaUseCase.executar(valorTotal, desconto, dataVenda, pedidosIds);

        // Then
        assertNotNull(resultado);
        assertEquals(2000.0, resultado.getValorTotal().getValor().doubleValue());
        assertEquals(200.0, resultado.getDesconto().getValor().doubleValue());
        assertEquals(dataVenda, resultado.getDataVenda());
        verify(repository).salvarVenda(any(Venda.class));
        verify(alterarStatusPedidoUseCase, times(3)).executar(any(UUID.class), eq(StatusPedido.CONCLUIDO));
    }

    @Test
    @DisplayName("Deve criar venda com desconto zero")
    void deveCriarVendaComDescontoZero() {
        // Given
        String valorTotal = "1000.0";
        String desconto = "0.0";
        LocalDate dataVenda = LocalDate.now();
        
        UUID vendaId = UUID.randomUUID();
        Venda vendaSalva = Venda.of(
                vendaId,
                com.zeroone.simlady.core.domain.venda.vendaVOs.ValorTotal.of(valorTotal),
                com.zeroone.simlady.core.domain.venda.vendaVOs.Desconto.of(desconto),
                false,
                dataVenda,
                new java.util.ArrayList<>(),
                java.time.LocalDateTime.now(),
                java.time.LocalDateTime.now()
        );

        when(repository.salvarVenda(any(Venda.class))).thenReturn(vendaSalva);

        // When
        Venda resultado = criarVendaUseCase.executar(valorTotal, desconto, dataVenda, null);

        // Then
        assertNotNull(resultado);
        assertEquals(1000.0, resultado.getValorTotal().getValor().doubleValue());
        assertEquals(0.0, resultado.getDesconto().getValor().doubleValue());
        verify(repository).salvarVenda(any(Venda.class));
    }
}
