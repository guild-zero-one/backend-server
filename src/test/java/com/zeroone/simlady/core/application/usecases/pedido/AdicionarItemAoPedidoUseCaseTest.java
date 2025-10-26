package com.zeroone.simlady.core.application.usecases.pedido;

import com.zeroone.simlady.core.application.ports.PedidoRepositoryPort;
import com.zeroone.simlady.core.domain.pedido.Pedido;
import com.zeroone.simlady.core.domain.pedido.PedidoItem;
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
class AdicionarItemAoPedidoUseCaseTest {

    @Mock
    private PedidoRepositoryPort repository;

    @InjectMocks
    private AdicionarItemAoPedidoUseCase adicionarItemAoPedidoUseCase;

    @Test
    @DisplayName("Deve adicionar item ao pedido com sucesso")
    void deveAdicionarItemAoPedidoComSucesso() {
        // Given
        UUID pedidoId = UUID.randomUUID();
        UUID idProduto = UUID.randomUUID();
        Integer quantidade = 2;
        String precoUnitario = "100.0";
        
        UUID idVenda = UUID.randomUUID();
        UUID idUsuario = UUID.randomUUID();
        
        Pedido pedido = Pedido.of(pedidoId, com.zeroone.simlady.core.domain.pedido.StatusPedido.PENDENTE, idVenda, idUsuario, 
                new java.util.ArrayList<>(), java.time.LocalDateTime.now(), java.time.LocalDateTime.now());
        
        Pedido pedidoAtualizado = Pedido.of(pedidoId, com.zeroone.simlady.core.domain.pedido.StatusPedido.PENDENTE, idVenda, idUsuario, 
                new java.util.ArrayList<>(), java.time.LocalDateTime.now(), java.time.LocalDateTime.now());
        pedidoAtualizado.adicionarItem(PedidoItem.newPedidoItem(idProduto, quantidade, precoUnitario));

        when(repository.buscarPorId(pedidoId)).thenReturn(Optional.of(pedido));
        when(repository.atualizarPedido(any(Pedido.class))).thenReturn(pedidoAtualizado);

        // When
        Pedido resultado = adicionarItemAoPedidoUseCase.executar(pedidoId, idProduto, quantidade, precoUnitario);

        // Then
        assertNotNull(resultado);
        assertEquals(pedidoId, resultado.getId());
        verify(repository).buscarPorId(pedidoId);
        verify(repository).atualizarPedido(any(Pedido.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando pedido não for encontrado")
    void deveLancarExcecaoQuandoPedidoNaoForEncontrado() {
        // Given
        UUID pedidoId = UUID.randomUUID();
        UUID idProduto = UUID.randomUUID();
        Integer quantidade = 2;
        String precoUnitario = "100.0";

        when(repository.buscarPorId(pedidoId)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, 
                () -> adicionarItemAoPedidoUseCase.executar(pedidoId, idProduto, quantidade, precoUnitario));
        
        assertEquals("Pedido não encontrado com ID: " + pedidoId, exception.getMessage());
        verify(repository).buscarPorId(pedidoId);
        verify(repository, never()).atualizarPedido(any(Pedido.class));
    }

    @Test
    @DisplayName("Deve adicionar múltiplos itens ao pedido")
    void deveAdicionarMultiplosItensAoPedido() {
        // Given
        UUID pedidoId = UUID.randomUUID();
        UUID idProduto1 = UUID.randomUUID();
        UUID idProduto2 = UUID.randomUUID();
        Integer quantidade1 = 2;
        Integer quantidade2 = 1;
        String precoUnitario1 = "100.0";
        String precoUnitario2 = "50.0";
        
        UUID idVenda = UUID.randomUUID();
        UUID idUsuario = UUID.randomUUID();
        
        Pedido pedido = Pedido.of(pedidoId, com.zeroone.simlady.core.domain.pedido.StatusPedido.PENDENTE, idVenda, idUsuario, 
                new java.util.ArrayList<>(), java.time.LocalDateTime.now(), java.time.LocalDateTime.now());
        pedido.adicionarItem(PedidoItem.newPedidoItem(idProduto1, quantidade1, precoUnitario1));
        
        Pedido pedidoAtualizado = Pedido.of(pedidoId, com.zeroone.simlady.core.domain.pedido.StatusPedido.PENDENTE, idVenda, idUsuario, 
                new java.util.ArrayList<>(), java.time.LocalDateTime.now(), java.time.LocalDateTime.now());
        pedidoAtualizado.adicionarItem(PedidoItem.newPedidoItem(idProduto1, quantidade1, precoUnitario1));
        pedidoAtualizado.adicionarItem(PedidoItem.newPedidoItem(idProduto2, quantidade2, precoUnitario2));

        when(repository.buscarPorId(pedidoId)).thenReturn(Optional.of(pedido));
        when(repository.atualizarPedido(any(Pedido.class))).thenReturn(pedidoAtualizado);

        // When
        Pedido resultado = adicionarItemAoPedidoUseCase.executar(pedidoId, idProduto2, quantidade2, precoUnitario2);

        // Then
        assertNotNull(resultado);
        assertEquals(pedidoId, resultado.getId());
        verify(repository).buscarPorId(pedidoId);
        verify(repository).atualizarPedido(any(Pedido.class));
    }
}
