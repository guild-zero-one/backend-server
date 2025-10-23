package com.zeroone.simlady.core.application.usecases.mensagem;

import com.zeroone.simlady.core.application.ports.MessagePublisherPort;
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
class EnviarPedidoCriadoUseCaseTest {

    @Mock
    private MessagePublisherPort messagePublisherPort;

    @InjectMocks
    private EnviarPedidoCriadoUseCase enviarPedidoCriadoUseCase;

    @Test
    @DisplayName("Deve enviar mensagem de pedido criado com sucesso")
    void deveEnviarMensagemDePedidoCriadoComSucesso() {
        // Given
        UUID idVenda = UUID.randomUUID();
        UUID idUsuario = UUID.randomUUID();
        UUID pedidoId = UUID.randomUUID();
        
        Pedido pedido = Pedido.of(pedidoId, com.zeroone.simlady.core.domain.pedido.StatusPedido.PENDENTE, idVenda, idUsuario, 
                new java.util.ArrayList<>(), java.time.LocalDateTime.now(), java.time.LocalDateTime.now());

        // When
        assertDoesNotThrow(() -> enviarPedidoCriadoUseCase.executar(pedido));

        // Then
        verify(messagePublisherPort).enviarPedidoCriado(pedido);
    }

    @Test
    @DisplayName("Deve enviar mensagem de pedido com status")
    void deveEnviarMensagemDePedidoComStatus() {
        // Given
        UUID idVenda = UUID.randomUUID();
        UUID idUsuario = UUID.randomUUID();
        UUID pedidoId = UUID.randomUUID();
        
        Pedido pedido = Pedido.of(pedidoId, com.zeroone.simlady.core.domain.pedido.StatusPedido.PENDENTE, idVenda, idUsuario, 
                new java.util.ArrayList<>(), java.time.LocalDateTime.now(), java.time.LocalDateTime.now());

        // When
        assertDoesNotThrow(() -> enviarPedidoCriadoUseCase.executar(pedido));

        // Then
        verify(messagePublisherPort).enviarPedidoCriado(pedido);
    }

    @Test
    @DisplayName("Deve enviar mensagem de pedido com itens")
    void deveEnviarMensagemDePedidoComItens() {
        // Given
        UUID idVenda = UUID.randomUUID();
        UUID idUsuario = UUID.randomUUID();
        UUID idProduto = UUID.randomUUID();
        UUID pedidoId = UUID.randomUUID();
        
        Pedido pedido = Pedido.of(pedidoId, com.zeroone.simlady.core.domain.pedido.StatusPedido.PENDENTE, idVenda, idUsuario, 
                new java.util.ArrayList<>(), java.time.LocalDateTime.now(), java.time.LocalDateTime.now());
        pedido.adicionarItem(com.zeroone.simlady.core.domain.pedido.PedidoItem.newPedidoItem(idProduto, 2, "100.0"));

        // When
        assertDoesNotThrow(() -> enviarPedidoCriadoUseCase.executar(pedido));

        // Then
        verify(messagePublisherPort).enviarPedidoCriado(pedido);
    }

    @Test
    @DisplayName("Deve enviar mensagem de pedido sem itens")
    void deveEnviarMensagemDePedidoSemItens() {
        // Given
        UUID idVenda = UUID.randomUUID();
        UUID idUsuario = UUID.randomUUID();
        UUID pedidoId = UUID.randomUUID();
        
        Pedido pedido = Pedido.of(pedidoId, com.zeroone.simlady.core.domain.pedido.StatusPedido.PENDENTE, idVenda, idUsuario, 
                new java.util.ArrayList<>(), java.time.LocalDateTime.now(), java.time.LocalDateTime.now());

        // When
        assertDoesNotThrow(() -> enviarPedidoCriadoUseCase.executar(pedido));

        // Then
        verify(messagePublisherPort).enviarPedidoCriado(pedido);
    }

    @Test
    @DisplayName("Deve executar envio sem retorno")
    void deveExecutarEnvioSemRetorno() {
        // Given
        UUID idVenda = UUID.randomUUID();
        UUID idUsuario = UUID.randomUUID();
        UUID pedidoId = UUID.randomUUID();
        
        Pedido pedido = Pedido.of(pedidoId, com.zeroone.simlady.core.domain.pedido.StatusPedido.PENDENTE, idVenda, idUsuario, 
                new java.util.ArrayList<>(), java.time.LocalDateTime.now(), java.time.LocalDateTime.now());

        // When
        enviarPedidoCriadoUseCase.executar(pedido);

        // Then
        verify(messagePublisherPort, times(1)).enviarPedidoCriado(pedido);
        verifyNoMoreInteractions(messagePublisherPort);
    }
}
