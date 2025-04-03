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

    @Bean
    TopicExchange mailingExchange() {
        return new TopicExchange("mailing");
    }

    @Bean
    public Queue passwordGeneratedMailingQueue() {
        return new Queue("mailing.password.generated", true);
    }

    @Bean
    public Queue genericGeneratedMailingQueue() {
        return new Queue("mailing.generic", true);
    }

    @Bean
    Binding passwordGeneratedMailingBinding(Queue passwordGeneratedMailingQueue, TopicExchange mailingExchange) {
        return BindingBuilder.bind(passwordGeneratedMailingQueue)
                .to(mailingExchange)
                .with("passwordGenerated");
    }

    @Bean
    Binding genericMailingBinding(Queue genericGeneratedMailingQueue, TopicExchange mailingExchange) {
        return BindingBuilder.bind(genericGeneratedMailingQueue)
                .to(mailingExchange)
                .with("generic");
    }

}
