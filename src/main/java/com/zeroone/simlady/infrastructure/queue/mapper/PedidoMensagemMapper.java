package com.zeroone.simlady.infrastructure.queue.mapper;

import com.zeroone.simlady.core.adapters.dtos.mensagem.PedidoMensagemDto;
import com.zeroone.simlady.core.domain.pedido.Pedido;
import com.zeroone.simlady.core.domain.pedido.PedidoItem;
import com.zeroone.simlady.core.domain.usuario.Usuario;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class PedidoMensagemMapper {

    public PedidoMensagemDto toMessageDto(Pedido pedido, Usuario usuario) {
        return new PedidoMensagemDto(
                pedido.getId(),
                usuario.getId(),
                usuario.getNome(),
                usuario.getCelular() != null ? Set.of(usuario.getCelular()) : Set.of(),
                pedido.getCriadoEm(),
                pedido.getStatus().name(),
                pedido.getItens().stream()
                        .map(PedidoItem::calcularSubtotal)
                        .map(preco -> preco.getValor().doubleValue())
                        .reduce(0.0, Double::sum)
        );
    }
}
