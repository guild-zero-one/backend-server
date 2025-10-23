package com.zeroone.simlady.infrastructure.queue;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class QueueSetup {

    public static final String ORDER_CREATED_QUEUE = "simlady-order-created";
    public static final String EXCHANGE_NAME = "simlady-exchange";

    @Bean
    public Queue orderCreatedQueue() {
        return new Queue(ORDER_CREATED_QUEUE, true);
    }

    @Bean
    public Exchange orderCreatedExchange() {
        return new org.springframework.amqp.core.DirectExchange(EXCHANGE_NAME, true, false);
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    @Lazy
    public RabbitTemplate rabbitTemplate(org.springframework.amqp.rabbit.connection.ConnectionFactory connectionFactory) {
        try {
            return new RabbitTemplate(connectionFactory);
        } catch (Exception e) {
            System.err.println("Falha ao criar RabbitTemplate: " + e.getMessage());
            return new FallbackRabbitTemplate();
        }
    }

    @Bean
    @Lazy
    public RabbitAdmin rabbitAdmin(RabbitTemplate rabbitTemplate) {
        try {
            RabbitAdmin rabbitAdmin = new RabbitAdmin(rabbitTemplate);
            rabbitAdmin.declareQueue(orderCreatedQueue());
            rabbitAdmin.declareExchange(orderCreatedExchange());
            System.out.println("Fila e Exchange criados com sucesso no RabbitMQ!");
            return rabbitAdmin;
        } catch (Exception e) {
            System.err.println("Falha ao criar RabbitAdmin: " + e.getMessage());
            return new RabbitAdmin(rabbitTemplate) {
                @Override
                public void afterPropertiesSet() {
                }
            };
        }
    }

    private static class FallbackRabbitTemplate extends RabbitTemplate {
        @Override
        public void convertAndSend(String exchange, Object message) {
            System.err.println("RabbitMQ indisponível. Mensagem não enviada.");
        }
    }
}
