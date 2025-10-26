package com.zeroone.simlady.infrastructure.queue;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import jakarta.annotation.PostConstruct;

@Configuration
public class QueueSetup {

    public static final String ORDER_CREATED_QUEUE = "simlady-order-created";
    public static final String EXCHANGE_NAME = "simlady-exchange";
    
    private RabbitAdmin rabbitAdmin;

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
    public RabbitTemplate rabbitTemplate(org.springframework.amqp.rabbit.connection.ConnectionFactory connectionFactory, Jackson2JsonMessageConverter jackson2JsonMessageConverter) {
        try {
            RabbitTemplate template = new RabbitTemplate(connectionFactory);
            template.setMessageConverter(jackson2JsonMessageConverter);
            return template;
        } catch (Exception e) {
            System.err.println("Falha ao criar RabbitTemplate: " + e.getMessage());
            return new FallbackRabbitTemplate();
        }
    }

    @Bean
    public RabbitAdmin rabbitAdmin(RabbitTemplate rabbitTemplate) {
        try {
            this.rabbitAdmin = new RabbitAdmin(rabbitTemplate);
            return this.rabbitAdmin;
        } catch (Exception e) {
            System.err.println("Falha ao criar RabbitAdmin: " + e.getMessage());
            return new RabbitAdmin(rabbitTemplate) {
                @Override
                public void afterPropertiesSet() {
                }
            };
        }
    }
    
    @PostConstruct
    public void initializeQueues() {
        if (rabbitAdmin != null) {
            try {
                rabbitAdmin.declareExchange(orderCreatedExchange());
                rabbitAdmin.declareQueue(orderCreatedQueue());
                rabbitAdmin.declareBinding(
                    new org.springframework.amqp.core.Binding(
                        ORDER_CREATED_QUEUE,
                        org.springframework.amqp.core.Binding.DestinationType.QUEUE,
                        EXCHANGE_NAME,
                        ORDER_CREATED_QUEUE,
                        null
                    )
                );
                System.out.println("Fila e Exchange criados com sucesso no RabbitMQ!");
            } catch (Exception e) {
                System.err.println("Falha ao inicializar filas: " + e.getMessage());
            }
        }
    }

    private static class FallbackRabbitTemplate extends RabbitTemplate {
        @Override
        public void convertAndSend(String exchange, Object message) {
            System.err.println("RabbitMQ indisponível. Mensagem não enviada.");
        }
    }
}
