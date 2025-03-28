package ehb.attendify.services.mailingservice.controllers;

import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@DependsOn("dynamic_bindings")
public class EventController {


    @RabbitListener(queues = "#{rabbitMQConfig.queueNames[T(ehb.attendify.services.mailingservice.configuration.Constants).EVENT_CREATE]}")
    public void onEventCreate(String name) {
        log.info("Event created with name {}", name);
    }

    @RabbitListener(queues = "#{rabbitMQConfig.queueNames[T(ehb.attendify.services.mailingservice.configuration.Constants).EVENT_REGISTER]}")
    public void onEventRegister(String name) {
        log.info("Event registered with name {}", name);
    }

    @RabbitListener(queues = "#{rabbitMQConfig.queueNames[T(ehb.attendify.services.mailingservice.configuration.Constants).EVENT_UNREGISTER]}")
    public void onEventUnregister(String name) {
        log.info("Event unregistered with name {}", name);
    }

    @RabbitListener(queues = "#{rabbitMQConfig.queueNames[T(ehb.attendify.services.mailingservice.configuration.Constants).EVENT_UPDATE]}")
    public void onEventUpdate(String name) {
        log.info("Event updated with name {}", name);
    }

    @RabbitListener(queues = "#{rabbitMQConfig.queueNames[T(ehb.attendify.services.mailingservice.configuration.Constants).EVENT_CANCEL]}")
    public void onEventCancel(String name) {
        log.info("Event cancelled with name {}", name);
    }

    @RabbitListener(queues = "#{rabbitMQConfig.queueNames[T(ehb.attendify.services.mailingservice.configuration.Constants).EVENT_MAILING_LIST]}")
    public void onEventMailingList(String name) {
        log.info("Event mailing list (?) with name {}", name);
    }



}
