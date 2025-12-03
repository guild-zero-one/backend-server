package com.zeroone.simlady.infrastructure.queue;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import jakarta.annotation.PostConstruct;

@Slf4j
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
    public DirectExchange orderCreatedExchange() {
        return new DirectExchange(EXCHANGE_NAME, true, false);
    }

    @Bean
    public Binding orderCreatedBinding(Queue orderCreatedQueue, DirectExchange orderCreatedExchange) {
        return BindingBuilder.bind(orderCreatedQueue)
                .to(orderCreatedExchange)
                .with(ORDER_CREATED_QUEUE);
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
                log.info("=== Inicializando RabbitMQ ===");

                DirectExchange exchange = orderCreatedExchange();
                Queue queue = orderCreatedQueue();

                rabbitAdmin.declareExchange(exchange);
                log.info("✓ Exchange declarada: {} (durable: {})", exchange.getName(), exchange.isDurable());

                rabbitAdmin.declareQueue(queue);
                log.info("✓ Queue declarada: {} (durable: {})", queue.getName(), queue.isDurable());

                Binding binding = orderCreatedBinding(queue, exchange);
                rabbitAdmin.declareBinding(binding);
                log.info("✓ Binding declarado: Queue '{}' -> Exchange '{}' com routing key '{}'",
                        queue.getName(), exchange.getName(), ORDER_CREATED_QUEUE);

                log.info("=== RabbitMQ inicializado com sucesso ===");
            } catch (Exception e) {
                log.error("✗ Falha ao inicializar filas: {}", e.getMessage(), e);
            }
        } else {
            log.warn("RabbitAdmin não disponível - pulando inicialização de filas");
        }
    }

    private static class FallbackRabbitTemplate extends RabbitTemplate {
        @Override
        public void convertAndSend(@NonNull String exchange, @NonNull Object message) {
            System.err.println("RabbitMQ indisponível. Mensagem não enviada. exchange=" + exchange + " message=" + message);
        }

        @Override
        public void convertAndSend(@NonNull String exchange, @NonNull String routingKey, @NonNull Object message) {
            System.err.println("RabbitMQ indisponível. Mensagem não enviada. exchange=" + exchange + " routingKey=" + routingKey + " message=" + message);
        }
    }
}
