package com.zeroone.simlady.core.application.usecases.pedido;

import com.zeroone.simlady.core.application.ports.PedidoRepositoryPort;
import com.zeroone.simlady.core.domain.pedido.Pedido;
import com.zeroone.simlady.core.domain.pedido.StatusPedido;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlterarStatusPedidoUseCaseTest {

    @Mock
    private PedidoRepositoryPort repository;

    @InjectMocks
    private AlterarStatusPedidoUseCase alterarStatusPedidoUseCase;

    @Test
    @DisplayName("Deve alterar status do pedido com sucesso")
    void deveAlterarStatusDoPedidoComSucesso() {
        // Given
        UUID pedidoId = UUID.randomUUID();
        StatusPedido novoStatus = StatusPedido.CONCLUIDO;
        
        UUID idVenda = UUID.randomUUID();
        UUID idUsuario = UUID.randomUUID();
        
        Pedido pedido = Pedido.of(pedidoId, StatusPedido.PENDENTE, idVenda, idUsuario, 
                new java.util.ArrayList<>(), java.time.LocalDateTime.now(), java.time.LocalDateTime.now());
        
        Pedido pedidoAtualizado = Pedido.of(pedidoId, novoStatus, idVenda, idUsuario, 
                new java.util.ArrayList<>(), java.time.LocalDateTime.now(), java.time.LocalDateTime.now());

        when(repository.buscarPorId(pedidoId)).thenReturn(Optional.of(pedido));
        when(repository.atualizarPedido(any(Pedido.class))).thenReturn(pedidoAtualizado);

        // When
        Pedido resultado = alterarStatusPedidoUseCase.executar(pedidoId, novoStatus);

        // Then
        assertNotNull(resultado);
        assertEquals(pedidoId, resultado.getId());
        assertEquals(novoStatus, resultado.getStatus());
        verify(repository).buscarPorId(pedidoId);
        verify(repository).atualizarPedido(any(Pedido.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando pedido não for encontrado")
    void deveLancarExcecaoQuandoPedidoNaoForEncontrado() {
        // Given
        UUID pedidoId = UUID.randomUUID();
        StatusPedido novoStatus = StatusPedido.CONCLUIDO;

        when(repository.buscarPorId(pedidoId)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, 
                () -> alterarStatusPedidoUseCase.executar(pedidoId, novoStatus));
        
        assertEquals("Pedido não encontrado com ID: " + pedidoId, exception.getMessage());
        verify(repository).buscarPorId(pedidoId);
        verify(repository, never()).atualizarPedido(any(Pedido.class));
    }

    @Test
    @DisplayName("Deve alterar status para CANCELADO")
    void deveAlterarStatusParaCancelado() {
        // Given
        UUID pedidoId = UUID.randomUUID();
        StatusPedido novoStatus = StatusPedido.CANCELADO;
        
        UUID idVenda = UUID.randomUUID();
        UUID idUsuario = UUID.randomUUID();
        
        Pedido pedido = Pedido.of(pedidoId, StatusPedido.PENDENTE, idVenda, idUsuario, 
                new java.util.ArrayList<>(), java.time.LocalDateTime.now(), java.time.LocalDateTime.now());
        
        Pedido pedidoAtualizado = Pedido.of(pedidoId, novoStatus, idVenda, idUsuario, 
                new java.util.ArrayList<>(), java.time.LocalDateTime.now(), java.time.LocalDateTime.now());

        when(repository.buscarPorId(pedidoId)).thenReturn(Optional.of(pedido));
        when(repository.atualizarPedido(any(Pedido.class))).thenReturn(pedidoAtualizado);

        // When
        Pedido resultado = alterarStatusPedidoUseCase.executar(pedidoId, novoStatus);

        // Then
        assertNotNull(resultado);
        assertEquals(StatusPedido.CANCELADO, resultado.getStatus());
        verify(repository).buscarPorId(pedidoId);
        verify(repository).atualizarPedido(any(Pedido.class));
    }

    @Test
    @DisplayName("Deve alterar status para PENDENTE")
    void deveAlterarStatusParaPendennte() {
        // Given
        UUID pedidoId = UUID.randomUUID();
        StatusPedido novoStatus = StatusPedido.PENDENTE;
        
        UUID idVenda = UUID.randomUUID();
        UUID idUsuario = UUID.randomUUID();
        
        Pedido pedido = Pedido.of(pedidoId, StatusPedido.PENDENTE, idVenda, idUsuario, 
                new java.util.ArrayList<>(), java.time.LocalDateTime.now(), java.time.LocalDateTime.now());
        
        Pedido pedidoAtualizado = Pedido.of(pedidoId, novoStatus, idVenda, idUsuario, 
                new java.util.ArrayList<>(), java.time.LocalDateTime.now(), java.time.LocalDateTime.now());

        when(repository.buscarPorId(pedidoId)).thenReturn(Optional.of(pedido));
        when(repository.atualizarPedido(any(Pedido.class))).thenReturn(pedidoAtualizado);

        // When
        Pedido resultado = alterarStatusPedidoUseCase.executar(pedidoId, novoStatus);

        // Then
        assertNotNull(resultado);
        assertEquals(StatusPedido.PENDENTE, resultado.getStatus());
        verify(repository).buscarPorId(pedidoId);
        verify(repository).atualizarPedido(any(Pedido.class));
    }
}
