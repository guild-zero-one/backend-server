package com.zeroone.simlady.core.application.usecases.pedido;

import com.zeroone.simlady.core.application.ports.PedidoRepositoryPort;
import com.zeroone.simlady.core.domain.pedido.Pedido;
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

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListarPedidosPorStatusUseCaseTest {

    @Mock
    private PedidoRepositoryPort repository;

    @InjectMocks
    private ListarPedidosPorStatusUseCase listarPedidosPorStatusUseCase;
    
    @BeforeEach
    void setUp() {
        reset(repository);
    }

    @Test
    @DisplayName("Deve listar pedidos por status com sucesso")
    void deveListarPedidosPorStatusComSucesso() {
        // Given
        String status = "PENDENTE";
        int pagina = 0;
        int tamanho = 10;
        
        UUID idVenda1 = UUID.randomUUID();
        UUID idUsuario1 = UUID.randomUUID();
        UUID idVenda2 = UUID.randomUUID();
        UUID idUsuario2 = UUID.randomUUID();
        
        UUID pedidoId1 = UUID.randomUUID();
        UUID pedidoId2 = UUID.randomUUID();
        
        Pedido pedido1 = Pedido.of(pedidoId1, com.zeroone.simlady.core.domain.pedido.StatusPedido.PENDENTE, idVenda1, idUsuario1, 
                new java.util.ArrayList<>(), java.time.LocalDateTime.now(), java.time.LocalDateTime.now());
        
        Pedido pedido2 = Pedido.of(pedidoId2, com.zeroone.simlady.core.domain.pedido.StatusPedido.PENDENTE, idVenda2, idUsuario2, 
                new java.util.ArrayList<>(), java.time.LocalDateTime.now(), java.time.LocalDateTime.now());

        List<Pedido> pedidos = List.of(pedido1, pedido2);
        Page<Pedido> page = new PageImpl<>(pedidos, PageRequest.of(pagina, tamanho), 2);

        when(repository.listarPorStatus(status, pagina, tamanho)).thenReturn(page);

        // When
        Page<Pedido> resultado = listarPedidosPorStatusUseCase.executar(status, pagina, tamanho);

        // Then
        assertNotNull(resultado);
        assertEquals(2, resultado.getTotalElements());
        assertEquals(2, resultado.getContent().size());
        assertEquals(com.zeroone.simlady.core.domain.pedido.StatusPedido.PENDENTE, resultado.getContent().get(0).getStatus());
        assertEquals(com.zeroone.simlady.core.domain.pedido.StatusPedido.PENDENTE, resultado.getContent().get(1).getStatus());
        verify(repository).listarPorStatus(status, pagina, tamanho);
    }

    @Test
    @DisplayName("Deve listar pedidos por status CONFIRMADO")
    void deveListarPedidosPorStatusConfirmado() {
        // Given
        String status = "CONFIRMADO";
        int pagina = 0;
        int tamanho = 10;
        
        UUID idVenda = UUID.randomUUID();
        UUID idUsuario = UUID.randomUUID();
        
        UUID pedidoId = UUID.randomUUID();
        Pedido pedido = Pedido.of(pedidoId, com.zeroone.simlady.core.domain.pedido.StatusPedido.CONCLUIDO, idVenda, idUsuario, 
                new java.util.ArrayList<>(), java.time.LocalDateTime.now(), java.time.LocalDateTime.now());

        List<Pedido> pedidos = List.of(pedido);
        Page<Pedido> page = new PageImpl<>(pedidos, PageRequest.of(pagina, tamanho), 1);

        when(repository.listarPorStatus(status, pagina, tamanho)).thenReturn(page);

        // When
        Page<Pedido> resultado = listarPedidosPorStatusUseCase.executar(status, pagina, tamanho);

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        assertEquals(1, resultado.getContent().size());
        assertEquals(com.zeroone.simlady.core.domain.pedido.StatusPedido.CONCLUIDO, resultado.getContent().get(0).getStatus());
        verify(repository).listarPorStatus(status, pagina, tamanho);
    }

    @Test
    @DisplayName("Deve retornar página vazia quando não há pedidos com o status")
    void deveRetornarPaginaVaziaQuandoNaoHaPedidosComOStatus() {
        // Given
        String status = "CANCELADO";
        int pagina = 0;
        int tamanho = 10;
        Page<Pedido> pageVazia = new PageImpl<>(List.of(), PageRequest.of(pagina, tamanho), 0);

        when(repository.listarPorStatus(status, pagina, tamanho)).thenReturn(pageVazia);

        // When
        Page<Pedido> resultado = listarPedidosPorStatusUseCase.executar(status, pagina, tamanho);

        // Then
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        assertEquals(0, resultado.getTotalElements());
        verify(repository).listarPorStatus(status, pagina, tamanho);
    }

    @Test
    @DisplayName("Deve listar pedidos por status com paginação")
    void deveListarPedidosPorStatusComPaginacao() {
        // Given
        String status = "ENTREGUE";
        int pagina = 1;
        int tamanho = 5;
        
        UUID idVenda = UUID.randomUUID();
        UUID idUsuario = UUID.randomUUID();
        
        UUID pedidoId = UUID.randomUUID();
        Pedido pedido = Pedido.of(pedidoId, com.zeroone.simlady.core.domain.pedido.StatusPedido.CONCLUIDO, idVenda, idUsuario, 
                new java.util.ArrayList<>(), java.time.LocalDateTime.now(), java.time.LocalDateTime.now());

        List<Pedido> pedidos = List.of(pedido);
        Page<Pedido> page = new PageImpl<>(pedidos, PageRequest.of(pagina, tamanho), 1);

        when(repository.listarPorStatus(eq(status), eq(pagina), eq(tamanho))).thenReturn(page);

        // When
        Page<Pedido> resultado = listarPedidosPorStatusUseCase.executar(status, pagina, tamanho);

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.getContent().size());
        assertEquals(pagina, resultado.getNumber());
        assertEquals(tamanho, resultado.getSize());
        assertEquals(com.zeroone.simlady.core.domain.pedido.StatusPedido.CONCLUIDO, resultado.getContent().get(0).getStatus());
        verify(repository).listarPorStatus(status, pagina, tamanho);
    }
}
