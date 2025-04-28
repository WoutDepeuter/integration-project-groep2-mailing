package ehb.attendify.services.mailingservice.configuration;

import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Log4j2
@Configuration
public class RabbitMQConfig {

    @Bean
    TopicExchange mailingExchange() {
        return new TopicExchange("mailing");
    }

    @Bean
    public Queue genericGeneratedMailingQueue() {
        return new Queue("mailing.generic", true);
    }

    @Bean
    Binding genericMailingBinding(Queue genericGeneratedMailingQueue, TopicExchange mailingExchange) {
        return BindingBuilder.bind(genericGeneratedMailingQueue)
                .to(mailingExchange)
                .with("generic");
    }

    @Bean
    public Queue mailingQueue() {
        return new Queue("mailing.mail");
    }

    @Bean
    public Queue userQueue() {
        return new Queue("mailing.user");
    }

    @Bean
    public Queue templateQueue() {
        return new Queue("mailing.template");
    }

}
