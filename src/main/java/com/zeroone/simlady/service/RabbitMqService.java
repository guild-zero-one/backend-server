package com.zeroone.simlady.service;

import com.zeroone.simlady.config.RabbitMq.RabbitMqConfig;
import com.zeroone.simlady.dto.pedido.PedidoMensagemDto;
import com.zeroone.simlady.entity.Contato;
import com.zeroone.simlady.entity.PedidoVenda;
import com.zeroone.simlady.entity.Usuario;
import com.zeroone.simlady.mapper.PedidoMensagemMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RabbitMqService {

    private final RabbitTemplate rabbitTemplate;
    private final PedidoMensagemMapper pedidoMensagemMapper;
    private final UsuarioService usuarioService;

    @CircuitBreaker(name = "rabbitMq", fallbackMethod = "fallbackEnviarPedidoCriado")
    public void enviarPedidoCriado(PedidoVenda pedido) {
        if (rabbitTemplate == null) {
            System.err.println("RabbitTemplate indisponível. Pedido não enviado para RabbitMQ.");
            return;
        }

        Usuario usuario = usuarioService.buscar(pedido.getUsuario().getId());

        PedidoMensagemDto message = pedidoMensagemMapper.toMessageDto(pedido);
        message.setNomeUsuario(usuario.getNome());
        message.setContatosUsuario(usuario.getContatos().stream().map(Contato::getCelular).collect(Collectors.toSet()));

        rabbitTemplate.convertAndSend(
                RabbitMqConfig.ORDER_CREATED_QUEUE,
                message
        );
    }

    public void fallbackEnviarPedidoCriado(PedidoVenda pedido, Throwable t){
        System.err.println("Falha ao enviar pedido para RabbitMQ: " + t.getMessage());
    }
}
