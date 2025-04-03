package ehb.attendify.services.mailingservice.controllers;

import ehb.attendify.services.mailingservice.models.GenericEmail;
import ehb.attendify.services.mailingservice.models.enums.ContentType;
import ehb.attendify.services.mailingservice.models.enums.Operation;
import ehb.attendify.services.mailingservice.models.general.AttendifyMessage;
import ehb.attendify.services.mailingservice.models.mail.body.Body;
import ehb.attendify.services.mailingservice.models.mail.header.Header;
import ehb.attendify.services.mailingservice.models.mail.header.Recipient;
import ehb.attendify.services.mailingservice.models.user.User;
import ehb.attendify.services.mailingservice.services.api.EmailService;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;

import static ehb.attendify.services.mailingservice.configuration.Constants.VALID_SENDERS;

@Log4j2
@Component
public class GenericMailingController {

    private final EmailService emailService;

    public GenericMailingController(EmailService emailService) {
        this.emailService = emailService;
    }

    @RabbitListener(queues = "#{genericGeneratedMailingQueue.name}")
    public void onGenericMail(GenericEmail email) {
        if (email.getHeader() == null || email.getHeader().getSender() == null) {
            return;
        }
        log.info("We've got an email! From {}, to {}", email.getHeader().getSender().getName(), email.getHeader().getRecipients());
        emailService.sendEmail(email);
    }

    @RabbitListener(queues = "#{passwordGeneratedMailingQueue.name}")
    public void onPasswordGenerated(AttendifyMessage<User> userAttendifyMessage) {
        log.debug("GenericMailingController#onPasswordGenerated called. Sender: {}, Operation: {}",
                userAttendifyMessage.getInfo().getSender(), userAttendifyMessage.getInfo().getOperation());

        if (!VALID_SENDERS.contains(userAttendifyMessage.getInfo().getSender().name().toLowerCase())) {
            return;
        }

        if (!userAttendifyMessage.getInfo().getOperation().equals(Operation.CREATE)) {
            return;
        }

        // Terrible code, this exact situation is for the templating stuff

        User user = userAttendifyMessage.getPayload();
        if (user == null) {
            log.error("We've received an empty user for a message");
            return;
        }

        GenericEmail email = GenericEmail.builder()
                .header(Header.builder()
                        .recipients(List.of(
                                Recipient.builder()
                                        .userId(user.getId())
                                        // TODO: Name should be formatted by a preference.
                                        .name(String.format("%s, %s", user.getLastName(), user.getFirstName()))
                                        .email(user.getEmail())
                                        .build()
                        ))
                        .subject("Password generated") // Is this correct?
                        .build())
                .body(Body.builder()
                        .contentType(ContentType.TEXT_PLAIN)
                        // ?????
                        .content(String.format("""
                                Your account has been created. Your password is: %s
                                """, user.getPassword()))
                        .build())
                .build();

        emailService.sendEmail(email);
    }

}
