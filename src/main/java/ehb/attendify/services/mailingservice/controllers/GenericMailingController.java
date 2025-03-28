package ehb.attendify.services.mailingservice.controllers;

import ehb.attendify.services.mailingservice.models.GenericEmail;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class GenericMailingController {

    @RabbitListener(queues = "mailing.mail")
    public void onGenericMail(GenericEmail email) {
        log.info("We've got an email! From {}, to {}", email.getHeader().getSender().getName(), email.getHeader().getRecipients());
    }

}
