package com.zeroone.simlady.core.application.usecases.pedido;

import com.zeroone.simlady.core.application.ports.PedidoRepositoryPort;
import com.zeroone.simlady.core.domain.pedido.Pedido;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AtualizarPedidoUseCaseTest {

    @Mock
    private PedidoRepositoryPort repository;

    @InjectMocks
    private AtualizarPedidoUseCase atualizarPedidoUseCase;

    @Test
    @DisplayName("Deve atualizar pedido com sucesso")
    void deveAtualizarPedidoComSucesso() {
        // Given
        UUID id = UUID.randomUUID();
        UUID idVenda = UUID.randomUUID();
        UUID idUsuario = UUID.randomUUID();
        
        Pedido pedido = Pedido.of(id, com.zeroone.simlady.core.domain.pedido.StatusPedido.PENDENTE, idVenda, idUsuario, 
                new java.util.ArrayList<>(), java.time.LocalDateTime.now(), java.time.LocalDateTime.now());
        
        Pedido pedidoAtualizado = Pedido.of(id, com.zeroone.simlady.core.domain.pedido.StatusPedido.CONCLUIDO, idVenda, idUsuario, 
                new java.util.ArrayList<>(), java.time.LocalDateTime.now(), java.time.LocalDateTime.now());

        when(repository.atualizarPedido(any(Pedido.class))).thenReturn(pedidoAtualizado);

        // When
        Pedido resultado = atualizarPedidoUseCase.executar(pedido);

        // Then
        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
        assertEquals(com.zeroone.simlady.core.domain.pedido.StatusPedido.CONCLUIDO, resultado.getStatus());
        verify(repository).atualizarPedido(pedido);
    }

    @Test
    @DisplayName("Deve atualizar pedido com novos itens")
    void deveAtualizarPedidoComNovosItens() {
        // Given
        UUID id = UUID.randomUUID();
        UUID idVenda = UUID.randomUUID();
        UUID idUsuario = UUID.randomUUID();
        UUID idProduto = UUID.randomUUID();
        
        Pedido pedido = Pedido.of(id, com.zeroone.simlady.core.domain.pedido.StatusPedido.PENDENTE, idVenda, idUsuario, 
                new java.util.ArrayList<>(), java.time.LocalDateTime.now(), java.time.LocalDateTime.now());
        pedido.adicionarItem(com.zeroone.simlady.core.domain.pedido.PedidoItem.newPedidoItem(idProduto, 2, "100.0"));
        
        Pedido pedidoAtualizado = Pedido.of(id, com.zeroone.simlady.core.domain.pedido.StatusPedido.PENDENTE, idVenda, idUsuario, 
                new java.util.ArrayList<>(), java.time.LocalDateTime.now(), java.time.LocalDateTime.now());
        pedidoAtualizado.adicionarItem(com.zeroone.simlady.core.domain.pedido.PedidoItem.newPedidoItem(idProduto, 3, "100.0"));

        when(repository.atualizarPedido(any(Pedido.class))).thenReturn(pedidoAtualizado);

        // When
        Pedido resultado = atualizarPedidoUseCase.executar(pedido);

        // Then
        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
        verify(repository).atualizarPedido(pedido);
    }

    @Test
    @DisplayName("Deve atualizar pedido com status alterado")
    void deveAtualizarPedidoComStatusAlterado() {
        // Given
        UUID id = UUID.randomUUID();
        UUID idVenda = UUID.randomUUID();
        UUID idUsuario = UUID.randomUUID();
        
        Pedido pedido = Pedido.of(id, com.zeroone.simlady.core.domain.pedido.StatusPedido.PENDENTE, idVenda, idUsuario, 
                new java.util.ArrayList<>(), java.time.LocalDateTime.now(), java.time.LocalDateTime.now());
        
        Pedido pedidoAtualizado = Pedido.of(id, com.zeroone.simlady.core.domain.pedido.StatusPedido.CONCLUIDO, idVenda, idUsuario, 
                new java.util.ArrayList<>(), java.time.LocalDateTime.now(), java.time.LocalDateTime.now());

        when(repository.atualizarPedido(any(Pedido.class))).thenReturn(pedidoAtualizado);

        // When
        Pedido resultado = atualizarPedidoUseCase.executar(pedido);

        // Then
        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
        assertEquals(com.zeroone.simlady.core.domain.pedido.StatusPedido.CONCLUIDO, resultado.getStatus());
        verify(repository).atualizarPedido(pedido);
    }
}
