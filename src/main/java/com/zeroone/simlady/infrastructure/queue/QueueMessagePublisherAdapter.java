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
            log.info("=== Iniciando envio de mensagem para pedido ===");
            log.info("Pedido ID: {}", pedido.getId());
            log.info("Usuário ID do pedido: {}", pedido.getIdUsuario());

            var usuarioOpt = usuarioRepositoryPort.buscarPorId(pedido.getIdUsuario());

            if (usuarioOpt.isEmpty()) {
                log.error("ERRO: Usuário não encontrado no banco de dados para ID: {}", pedido.getIdUsuario());
                throw new ResourceNotFoundException("Usuário não encontrado com ID: " + pedido.getIdUsuario());
            }

            var usuario = usuarioOpt.get();
            log.info("Usuário encontrado - ID: {}, Nome: {}, Email: {}",
                    usuario.getId(), usuario.getNome(), usuario.getEmail());

            PedidoMensagemDto message = pedidoMensagemMapper.toMessageDto(pedido, usuario);
            log.info("Mensagem mapeada com sucesso");
            log.debug("Conteúdo da mensagem: {}", message);

            log.info("Enviando para RabbitMQ - Exchange: {}, RoutingKey: {}",
                    QueueSetup.EXCHANGE_NAME, QueueSetup.ORDER_CREATED_QUEUE);

            rabbitTemplate.convertAndSend(
                    QueueSetup.EXCHANGE_NAME,
                    QueueSetup.ORDER_CREATED_QUEUE,
                    message
            );

            log.info("✓ Mensagem de pedido criado enviada com sucesso para pedido: {}", pedido.getId());
        } catch (ResourceNotFoundException e) {
            log.error("✗ Usuário não encontrado: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("✗ Erro ao enviar mensagem de pedido criado: {}", e.getMessage(), e);
            throw e;
        }
    }

    public void fallbackEnviarPedidoCriado(Pedido pedido, Throwable t) {
        log.error("Falha ao enviar pedido para RabbitMQ: {}", t.getMessage());
    }
}
