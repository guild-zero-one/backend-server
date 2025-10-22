package com.zeroone.simlady.service_test;

import com.zeroone.simlady.config.RabbitMq.RabbitMqConfig;
import com.zeroone.simlady.dto.pedido.PedidoMensagemDto;
import com.zeroone.simlady.entity.Contato;
import com.zeroone.simlady.entity.PedidoVenda;
import com.zeroone.simlady.entity.Usuario;
import com.zeroone.simlady.mapper.PedidoMensagemMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.Set;

import static org.mockito.Mockito.*;

class RabbitMqServiceTest {

    @Mock
    private RabbitTemplate rabbitTemplate;
    @Mock
    private PedidoMensagemMapper pedidoMensagemMapper;
    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private RabbitMqService rabbitMqService;

    @Test
    @DisplayName("Deve enviar mensagem de pedido criado para o RabbitMQ")
    void deveEnviarMensagemPedidoCriado() {
        MockitoAnnotations.openMocks(this);

        PedidoVenda pedido = new PedidoVenda();
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setNome("Maria");
        Contato contato = new Contato();
        contato.setCelular("11999999999");
        usuario.setContatos(Set.of(contato));
        pedido.setUsuario(usuario);

        PedidoMensagemDto mensagemDto = new PedidoMensagemDto();
        when(usuarioService.buscar(1)).thenReturn(usuario);
        when(pedidoMensagemMapper.toMessageDto(pedido)).thenReturn(mensagemDto);

        rabbitMqService.enviarPedidoCriado(pedido);

        verify(usuarioService).buscar(1);
        verify(pedidoMensagemMapper).toMessageDto(pedido);
        verify(rabbitTemplate).convertAndSend(
                eq(RabbitMqConfig.ORDER_CREATED_QUEUE),
                argThat((Object msg) -> {
                    PedidoMensagemDto dto = (PedidoMensagemDto) msg;
                    return "Maria".equals(dto.getNomeUsuario()) &&
                            dto.getContatosUsuario().contains("11999999999");
                })
        );
    }
}