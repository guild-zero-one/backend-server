package com.zeroone.simlady.service;

import com.zeroone.simlady.config.RabbitMq.RabbitMqConfig;
import com.zeroone.simlady.dto.pedido.PedidoMensagemDto;
import com.zeroone.simlady.entity.Contato;
import com.zeroone.simlady.entity.PedidoVenda;
import com.zeroone.simlady.entity.Usuario;
import com.zeroone.simlady.mapper.PedidoMensagemMapper;
import com.zeroone.simlady.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RabbitMqService {

    private final RabbitTemplate rabbitTemplate;
    private final PedidoMensagemMapper pedidoMensagemMapper;
    private final UsuarioService usuarioService;


    public void enviarPedidoCriado(PedidoVenda pedido) {
        Usuario usuario = usuarioService.buscar(pedido.getUsuario().getId());

        PedidoMensagemDto message = pedidoMensagemMapper.toMessageDto(pedido);
        message.setNomeUsuario(usuario.getNome());
        message.setContatosUsuario(usuario.getContatos().stream().map(Contato::getCelular).collect(Collectors.toSet()));

        rabbitTemplate.convertAndSend(
                RabbitMqConfig.ORDER_CREATED_QUEUE,
                message
        );
    }
}