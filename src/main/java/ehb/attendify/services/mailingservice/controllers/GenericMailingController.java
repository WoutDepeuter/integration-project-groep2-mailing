package ehb.attendify.services.mailingservice.controllers;

import ehb.attendify.services.mailingservice.models.GenericEmail;
import ehb.attendify.services.mailingservice.services.api.EmailService;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class GenericMailingController {

    private final EmailService emailService;

    public GenericMailingController(EmailService emailService) {
        this.emailService = emailService;
    }

    @RabbitListener(queues = "mailing.mail")
    public void onGenericMail(GenericEmail email) {
        log.info("We've got an email! From {}, to {}", email.getHeader().getSender().getName(), email.getHeader().getRecipients());
        emailService.sendEmail(email);
    }

}
