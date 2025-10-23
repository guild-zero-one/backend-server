package com.zeroone.simlady.core.application.usecases.pedido;

import com.zeroone.simlady.core.application.ports.PedidoRepositoryPort;
import com.zeroone.simlady.core.application.ports.ProdutoRepositoryPort;
import com.zeroone.simlady.core.domain.pedido.Pedido;
import com.zeroone.simlady.core.domain.pedido.PedidoItem;
import com.zeroone.simlady.infrastructure.exception.ResourceNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CriarPedidoUseCaseTest {

    @Mock
    private PedidoRepositoryPort repository;

    @Mock
    private ProdutoRepositoryPort produtoRepositoryPort;

    @InjectMocks
    private CriarPedidoUseCase criarPedidoUseCase;

    @Test
    @DisplayName("Deve criar pedido com sucesso")
    void deveCriarPedidoComSucesso() {
        // Given
        UUID idVenda = UUID.randomUUID();
        UUID idUsuario = UUID.randomUUID();
        UUID idProduto = UUID.randomUUID();
        
        PedidoItem item = PedidoItem.of(UUID.randomUUID(), idProduto, 2, "100.0");
        
        List<PedidoItem> itens = List.of(item);
        
        UUID pedidoId = UUID.randomUUID();
        Pedido pedidoSalvo = Pedido.of(pedidoId, com.zeroone.simlady.core.domain.pedido.StatusPedido.PENDENTE, idVenda, idUsuario, 
                new java.util.ArrayList<>(), java.time.LocalDateTime.now(), java.time.LocalDateTime.now());
        pedidoSalvo.adicionarItem(item);

        when(produtoRepositoryPort.buscarPorId(idProduto)).thenReturn(Optional.of(mock(com.zeroone.simlady.core.domain.produto.Produto.class)));
        when(repository.salvarPedido(any(Pedido.class))).thenReturn(pedidoSalvo);

        // When
        Pedido resultado = criarPedidoUseCase.executar(idVenda, idUsuario, itens);

        // Then
        assertNotNull(resultado);
        assertEquals(idVenda, resultado.getIdVenda());
        assertEquals(idUsuario, resultado.getIdUsuario());
        verify(produtoRepositoryPort).buscarPorId(idProduto);
        verify(repository).salvarPedido(any(Pedido.class));
    }

    @Test
    @DisplayName("Deve criar pedido sem itens")
    void deveCriarPedidoSemItens() {
        // Given
        UUID idVenda = UUID.randomUUID();
        UUID idUsuario = UUID.randomUUID();
        
        UUID pedidoId = UUID.randomUUID();
        Pedido pedidoSalvo = Pedido.of(pedidoId, com.zeroone.simlady.core.domain.pedido.StatusPedido.PENDENTE, idVenda, idUsuario, 
                new java.util.ArrayList<>(), java.time.LocalDateTime.now(), java.time.LocalDateTime.now());

        when(repository.salvarPedido(any(Pedido.class))).thenReturn(pedidoSalvo);

        // When
        Pedido resultado = criarPedidoUseCase.executar(idVenda, idUsuario, null);

        // Then
        assertNotNull(resultado);
        assertEquals(idVenda, resultado.getIdVenda());
        assertEquals(idUsuario, resultado.getIdUsuario());
        verify(repository).salvarPedido(any(Pedido.class));
        verify(produtoRepositoryPort, never()).buscarPorId(any(UUID.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando produto não for encontrado")
    void deveLancarExcecaoQuandoProdutoNaoForEncontrado() {
        // Given
        UUID idVenda = UUID.randomUUID();
        UUID idUsuario = UUID.randomUUID();
        UUID idProduto = UUID.randomUUID();
        
        PedidoItem item = PedidoItem.of(UUID.randomUUID(), idProduto, 2, "100.0");
        
        List<PedidoItem> itens = List.of(item);

        when(produtoRepositoryPort.buscarPorId(idProduto)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, 
                () -> criarPedidoUseCase.executar(idVenda, idUsuario, itens));
        
        assertEquals("Produto não encontrado com ID: " + idProduto, exception.getMessage());
        verify(produtoRepositoryPort).buscarPorId(idProduto);
        verify(repository, never()).salvarPedido(any(Pedido.class));
    }

    @Test
    @DisplayName("Deve criar pedido com múltiplos itens")
    void deveCriarPedidoComMultiplosItens() {
        // Given
        UUID idVenda = UUID.randomUUID();
        UUID idUsuario = UUID.randomUUID();
        UUID idProduto1 = UUID.randomUUID();
        UUID idProduto2 = UUID.randomUUID();
        
        PedidoItem item1 = PedidoItem.of(UUID.randomUUID(), idProduto1, 2, "100.0");
        PedidoItem item2 = PedidoItem.of(UUID.randomUUID(), idProduto2, 1, "50.0");
        
        List<PedidoItem> itens = List.of(item1, item2);
        
        UUID pedidoId = UUID.randomUUID();
        Pedido pedidoSalvo = Pedido.of(pedidoId, com.zeroone.simlady.core.domain.pedido.StatusPedido.PENDENTE, idVenda, idUsuario, 
                new java.util.ArrayList<>(), java.time.LocalDateTime.now(), java.time.LocalDateTime.now());
        pedidoSalvo.adicionarItem(item1);
        pedidoSalvo.adicionarItem(item2);

        when(produtoRepositoryPort.buscarPorId(idProduto1)).thenReturn(Optional.of(mock(com.zeroone.simlady.core.domain.produto.Produto.class)));
        when(produtoRepositoryPort.buscarPorId(idProduto2)).thenReturn(Optional.of(mock(com.zeroone.simlady.core.domain.produto.Produto.class)));
        when(repository.salvarPedido(any(Pedido.class))).thenReturn(pedidoSalvo);

        // When
        Pedido resultado = criarPedidoUseCase.executar(idVenda, idUsuario, itens);

        // Then
        assertNotNull(resultado);
        assertEquals(idVenda, resultado.getIdVenda());
        assertEquals(idUsuario, resultado.getIdUsuario());
        verify(produtoRepositoryPort).buscarPorId(idProduto1);
        verify(produtoRepositoryPort).buscarPorId(idProduto2);
        verify(repository).salvarPedido(any(Pedido.class));
    }
}
