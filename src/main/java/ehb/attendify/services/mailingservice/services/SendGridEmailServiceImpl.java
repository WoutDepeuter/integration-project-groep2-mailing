package ehb.attendify.services.mailingservice.services;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGridAPI;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import ehb.attendify.services.mailingservice.models.GenericEmail;
import ehb.attendify.services.mailingservice.models.mail.header.Recipient;
import ehb.attendify.services.mailingservice.services.api.EmailService;
import ehb.attendify.services.mailingservice.services.api.MetricService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Log4j2
@Service
public class SendGridEmailServiceImpl implements EmailService {

    private final MetricService metricService;
    private final SendGridAPI sendGrid;
    private final String fromEmail;

    public SendGridEmailServiceImpl(SendGridAPI sendGrid, @Value("${sendgrid.from-email}") String fromEmail, MetricService metricService) {
        this.sendGrid = sendGrid;
        this.fromEmail = fromEmail;
        this.metricService = metricService;

        if (this.fromEmail == null || this.fromEmail.isEmpty()) {
            log.warn("Sender email address is null or empty, check if this is intended");
        }
    }

    @Override
    public void sendEmail(GenericEmail email) {
        log.debug("SendGridEmailServiceImpl#sendEmail received {}, sending from {}", email, fromEmail);

        Email from = new Email(fromEmail);
        Mail mail = new Mail();
        mail.setFrom(from);
        mail.setSubject(email.getHeader().getSubject());

        Personalization personalization = new Personalization();
        if (email.getHeader().getRecipients() == null || email.getHeader().getRecipients().isEmpty()) {
            throw new IllegalArgumentException("No recipient");
        }

        for (Recipient recipient : email.getHeader().getRecipients()) {
            personalization.addTo(new Email(recipient.getEmail()));
        }

        if (email.getHeader().getCc() != null) {
            for (Recipient ccRecipient : email.getHeader().getCc()) {
                personalization.addCc(new Email(ccRecipient.getEmail()));
            }
        }

        if (email.getHeader().getBcc() != null) {
            for (Recipient bccRecipient : email.getHeader().getBcc()) {
                personalization.addBcc(new Email(bccRecipient.getEmail()));
            }
        }

        mail.addPersonalization(personalization);
        Content emailContent = new Content(email.getBody().getContentType().getType(), email.getBody().getContent());
        mail.addContent(emailContent);

        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sendGrid.api(request);

            if (response.getStatusCode() >= 200 && response.getStatusCode() < 300) {
                log.debug("Email was send successfully: {}", response.getBody());
                this.metricService.getMailCounter().increment();
                return;
            }

            log.error("SendGrid send back a non positive response code {}, Body: {}, Headers: {}",
                    response.getStatusCode(), response.getBody(), response.getHeaders());
        } catch (IOException ex) {
            log.error("Error sending email via SendGrid", ex);
        }
    }
}
