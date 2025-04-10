package ehb.attendify.services.mailingservice.controllers;

import ehb.attendify.services.mailingservice.models.GenericEmail;
import ehb.attendify.services.mailingservice.services.api.EmailService;
import ehb.attendify.services.mailingservice.services.api.FormatService;
import ehb.attendify.services.mailingservice.services.api.MessageMapperService;
import ehb.attendify.services.mailingservice.services.api.TemplateService;
import ehb.attendify.services.mailingservice.services.api.UnknownMessageSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@RequiredArgsConstructor
public class GenericMailingController {

    private final EmailService emailService;
    private final TemplateService templateService;
    private final FormatService formatService;
    private final MessageMapperService messageMapperService;

    @RabbitListener(queues = "#{genericGeneratedMailingQueue.name}")
    public void onGenericMail(GenericEmail email) {
        if (email.getHeader() == null || email.getHeader().getSender() == null) {
            return;
        }
        log.info("We've got an email! From {}, to {}", email.getHeader().getSender().getName(), email.getHeader().getRecipients());
        emailService.sendEmail(email);
    }

    @RabbitListener(queues = "mailing.mail")
    public void onGenericEvent(Message message) {
        var exchange = message.getMessageProperties().getReceivedExchange();
        var routingKey = message.getMessageProperties().getReceivedRoutingKey();

        var optionalTemplate = templateService.getTemplate(exchange, routingKey);

        if (optionalTemplate.isEmpty()) {
            log.debug("Received an event with no configured template on {} via {}", exchange, routingKey);
            return;
        }

        var template = optionalTemplate.get();

        Object obj;
        try {
            obj = messageMapperService.map(message);
        } catch (UnknownMessageSource unknownMessageSource) {
            log.error("Failed to map message on {} via {}", exchange, routingKey, unknownMessageSource);
            return;
        }


        var email = formatService.formatEmail(template, obj);
        this.emailService.sendEmail(email);
    }

}
