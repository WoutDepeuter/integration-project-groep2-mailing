package ehb.attendify.services.mailingservice.configuration;

import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AnonymousQueue;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@Configuration
public class RabbitMQConfig {

    private final String EXCHANGE_NAME = "event";
    public final List<String> ROUTING_KEYS = List.of(
            Constants.EVENT_REGISTER,
            Constants.EVENT_UNREGISTER,
            Constants.EVENT_CREATE,
            Constants.EVENT_UPDATE,
            Constants.EVENT_CANCEL,
            Constants.EVENT_MAILING_LIST,
            Constants.SESSION_REGISTER,
            Constants.SESSION_UNREGISTER,
            Constants.SESSION_CREATE,
            Constants.SESSION_UPDATE,
            Constants.SESSION_CANCEL,
            Constants.SESSION_DELAY,
            Constants.INVOICE_SEND
    );
    public final Map<String, String> queueNames = new HashMap<>();

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue mailingQueue() {
        return new Queue("mailing.mail", true);
    }

    @Bean
    public List<Binding> mailingQueueBindings(TopicExchange exchange, Queue mailingQueue) {
        List<Binding> bindings = new ArrayList<>();

        for (String routingKey : ROUTING_KEYS) {
            Binding binding = BindingBuilder.bind(mailingQueue).to(exchange).with(routingKey);
            bindings.add(binding);
            log.debug("Queue {} has been bound to exchange {} with routing key {}", mailingQueue.getName(), exchange.getName(), routingKey);
        }

        return bindings;
    }

    @Bean("dynamic_bindings")
    List<Binding> bindings(TopicExchange exchange, AmqpAdmin amqpAdmin) {
        if (ROUTING_KEYS.isEmpty()) {
            log.warn("No routing keys have been found in the RabbitMQConfig, not configuring any queues");
            return List.of();
        }

        List<Binding> bindings = new ArrayList<>();
        for (String key : ROUTING_KEYS) {
            AnonymousQueue queue = new AnonymousQueue();
            bindings.add(BindingBuilder.bind(queue).to(exchange).with(key));

            amqpAdmin.declareQueue(queue);
            queueNames.put(key, queue.getName());
            log.debug("Anonymous queue {} has been bounded to {} with {}", queue.getName(), exchange.getName(), key);
        }

        log.info("{} anonymous queues have been configured for this sessions", queueNames.size());
        return bindings;
    }

}
