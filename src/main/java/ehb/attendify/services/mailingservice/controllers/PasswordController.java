package ehb.attendify.services.mailingservice.controllers;

import ehb.attendify.services.mailingservice.configuration.Constants;
import ehb.attendify.services.mailingservice.models.GenericEmail;
import ehb.attendify.services.mailingservice.models.enums.Operation;
import ehb.attendify.services.mailingservice.models.enums.Sender;
import ehb.attendify.services.mailingservice.models.general.AttendifyMessage;
import ehb.attendify.services.mailingservice.models.template.Template;
import ehb.attendify.services.mailingservice.models.user.User;
import ehb.attendify.services.mailingservice.services.api.EmailService;
import ehb.attendify.services.mailingservice.services.api.FormatService;
import ehb.attendify.services.mailingservice.services.api.TemplateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Log4j2
@Component
@RequiredArgsConstructor
public class PasswordController {

    private final EmailService emailService;
    private final TemplateService templateService;
    private final FormatService formatService;

    @RabbitListener(queues = "mailing.password.generated")
    public void onPasswordGenerated(AttendifyMessage<User> userAttendifyMessage) {
        log.debug("GenericMailingController#onPasswordGenerated called. Sender: {}, Operation: {}",
                userAttendifyMessage.getInfo().getSender(), userAttendifyMessage.getInfo().getOperation());

        if (!List.of(Sender.CRM, Sender.POS).contains(userAttendifyMessage.getInfo().getSender())) {
            return;
        }

        if (!userAttendifyMessage.getInfo().getOperation().equals(Operation.CREATE)) {
            return;
        }

        User user = userAttendifyMessage.getUser();
        if (user == null) {
            log.error("We've received an empty user for a message");
            return;
        }

        Optional<Template> optionalTemplate = this.templateService.getTemplate("mailing", "passwordGenerated");
        if (optionalTemplate.isEmpty()) {
            log.error("Received a message on {} with no template bounded, contact the {}-team",
                    "mailing.password.generated",
                    userAttendifyMessage.getInfo().getSender());
            return;
        }

        var template = optionalTemplate.get();
        GenericEmail email = this.formatService.formatSimpleEmail(template, user, user);
        emailService.sendEmail(email);
    }

}
