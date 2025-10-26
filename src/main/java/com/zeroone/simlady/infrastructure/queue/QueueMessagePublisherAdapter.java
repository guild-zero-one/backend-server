package com.zeroone.simlady.infrastructure.queue;

import com.zeroone.simlady.core.adapters.dtos.mensagem.PedidoMensagemDto;
import com.zeroone.simlady.core.application.ports.MessagePublisherPort;
import com.zeroone.simlady.core.application.ports.UsuarioRepositoryPort;
import com.zeroone.simlady.core.domain.pedido.Pedido;
import com.zeroone.simlady.infrastructure.exception.ResourceNotFoundException;
import com.zeroone.simlady.infrastructure.queue.mapper.PedidoMensagemMapper;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class QueueMessagePublisherAdapter implements MessagePublisherPort {

    private final RabbitTemplate rabbitTemplate;
    private final PedidoMensagemMapper pedidoMensagemMapper;
    private final UsuarioRepositoryPort usuarioRepositoryPort;

    @Override
    @CircuitBreaker(name = "rabbitMq", fallbackMethod = "fallbackEnviarPedidoCriado")
    public void enviarPedidoCriado(Pedido pedido) {
        try {
            var usuario = usuarioRepositoryPort.buscarPorId(pedido.getIdUsuario())
                    .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

            PedidoMensagemDto message = pedidoMensagemMapper.toMessageDto(pedido, usuario);

            rabbitTemplate.convertAndSend(
                    QueueSetup.EXCHANGE_NAME,
                    QueueSetup.ORDER_CREATED_QUEUE,
                    message
            );

            log.info("Mensagem de pedido criado enviada com sucesso: {}", pedido.getId());
        } catch (Exception e) {
            log.error("Erro ao enviar mensagem de pedido criado: {}", e.getMessage());
            throw e;
        }
    }

    public void fallbackEnviarPedidoCriado(Pedido pedido, Throwable t) {
        log.error("Falha ao enviar pedido para RabbitMQ: {}", t.getMessage());
    }
}
