package ehb.attendify.services.mailingservice.controllers;

import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@DependsOn("dynamic_bindings")
public class SessionController {

    @RabbitListener(queues = "#{rabbitMQConfig.queueNames[T(ehb.attendify.services.mailingservice.configuration.Constants).SESSION_REGISTER]}")
    public void onSessionRegister(String name) {
        log.info("Sessions registered with {}", name);
    }

    @RabbitListener(queues = "#{rabbitMQConfig.queueNames[T(ehb.attendify.services.mailingservice.configuration.Constants).SESSION_UNREGISTER]}")
    public void onSessionUnregister(String name) {
        log.info("Sessions unregistered with {}", name);
    }

    @RabbitListener(queues = "#{rabbitMQConfig.queueNames[T(ehb.attendify.services.mailingservice.configuration.Constants).SESSION_CREATE]}")
    public void onSessionCreated(String name) {
        log.info("Sessions created with {}", name);
    }

    @RabbitListener(queues = "#{rabbitMQConfig.queueNames[T(ehb.attendify.services.mailingservice.configuration.Constants).SESSION_UPDATE]}")
    public void onSessionUpdated(String name) {
        log.info("Sessions updated with {}", name);
    }

    @RabbitListener(queues = "#{rabbitMQConfig.queueNames[T(ehb.attendify.services.mailingservice.configuration.Constants).SESSION_CANCEL]}")
    public void onSessionCancel(String name) {
        log.info("Sessions cancelled with {}", name);
    }

    @RabbitListener(queues = "#{rabbitMQConfig.queueNames[T(ehb.attendify.services.mailingservice.configuration.Constants).SESSION_DELAY]}")
    public void onSessionDelay(String name) {
        log.info("Sessions delayed with {}", name);
    }

}
