package com.zeroone.simlady.core.application.usecases.mensagem;

import com.zeroone.simlady.core.application.ports.MessagePublisherPort;
import com.zeroone.simlady.core.domain.pedido.Pedido;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EnviarPedidoCriadoUseCase {

    private final MessagePublisherPort messagePublisherPort;

    public void executar(Pedido pedido) {
        messagePublisherPort.enviarPedidoCriado(pedido);
    }
}
