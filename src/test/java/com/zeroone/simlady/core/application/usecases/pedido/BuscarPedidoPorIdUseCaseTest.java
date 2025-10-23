package com.zeroone.simlady.core.application.usecases.pedido;

import com.zeroone.simlady.core.application.ports.PedidoRepositoryPort;
import com.zeroone.simlady.core.domain.pedido.Pedido;
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
class BuscarPedidoPorIdUseCaseTest {

    @Mock
    private PedidoRepositoryPort repository;

    @InjectMocks
    private BuscarPedidoPorIdUseCase buscarPedidoPorIdUseCase;

    @Test
    @DisplayName("Deve buscar pedido por ID com sucesso")
    void deveBuscarPedidoPorIdComSucesso() {
        // Given
        UUID id = UUID.randomUUID();
        UUID idVenda = UUID.randomUUID();
        UUID idUsuario = UUID.randomUUID();
        
        Pedido pedido = Pedido.of(id, com.zeroone.simlady.core.domain.pedido.StatusPedido.PENDENTE, idVenda, idUsuario, 
                new java.util.ArrayList<>(), java.time.LocalDateTime.now(), java.time.LocalDateTime.now());

        when(repository.buscarPorId(id)).thenReturn(Optional.of(pedido));

        // When
        Pedido resultado = buscarPedidoPorIdUseCase.executar(id);

        // Then
        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
        assertEquals(idVenda, resultado.getIdVenda());
        assertEquals(idUsuario, resultado.getIdUsuario());
        verify(repository).buscarPorId(id);
    }

    @Test
    @DisplayName("Deve lançar exceção quando pedido não for encontrado")
    void deveLancarExcecaoQuandoPedidoNaoForEncontrado() {
        // Given
        UUID id = UUID.randomUUID();
        when(repository.buscarPorId(id)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, 
                () -> buscarPedidoPorIdUseCase.executar(id));
        
        assertEquals("Pedido não encontrado com ID: " + id, exception.getMessage());
        verify(repository).buscarPorId(id);
    }

    @Test
    @DisplayName("Deve buscar pedido com status")
    void deveBuscarPedidoComStatus() {
        // Given
        UUID id = UUID.randomUUID();
        UUID idVenda = UUID.randomUUID();
        UUID idUsuario = UUID.randomUUID();
        
        Pedido pedido = Pedido.of(id, com.zeroone.simlady.core.domain.pedido.StatusPedido.PENDENTE, idVenda, idUsuario, 
                new java.util.ArrayList<>(), java.time.LocalDateTime.now(), java.time.LocalDateTime.now());
        pedido.alterarStatus(com.zeroone.simlady.core.domain.pedido.StatusPedido.PENDENTE);

        when(repository.buscarPorId(id)).thenReturn(Optional.of(pedido));

        // When
        Pedido resultado = buscarPedidoPorIdUseCase.executar(id);

        // Then
        assertNotNull(resultado);
        assertEquals(com.zeroone.simlady.core.domain.pedido.StatusPedido.PENDENTE, resultado.getStatus());
        verify(repository).buscarPorId(id);
    }
}
