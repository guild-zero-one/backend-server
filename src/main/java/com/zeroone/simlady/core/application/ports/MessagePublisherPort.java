package com.zeroone.simlady.core.application.ports;

import com.zeroone.simlady.core.domain.pedido.Pedido;

public interface MessagePublisherPort {

    void enviarPedidoCriado(Pedido pedido);
}
