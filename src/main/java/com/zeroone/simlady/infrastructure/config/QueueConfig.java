package com.zeroone.simlady.infrastructure.config;

import com.zeroone.simlady.core.application.ports.MessagePublisherPort;
import com.zeroone.simlady.core.application.usecases.mensagem.EnviarPedidoCriadoUseCase;
import com.zeroone.simlady.infrastructure.queue.QueueMessagePublisherAdapter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueueConfig {

    @Bean
    public MessagePublisherPort messagePublisherPort(QueueMessagePublisherAdapter adapter) {
        return adapter;
    }

    @Bean
    public EnviarPedidoCriadoUseCase enviarPedidoCriadoUseCase(MessagePublisherPort messagePublisherPort) {
        return new EnviarPedidoCriadoUseCase(messagePublisherPort);
    }
}