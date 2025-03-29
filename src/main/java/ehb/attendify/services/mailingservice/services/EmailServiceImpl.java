package ehb.attendify.services.mailingservice.services;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import ehb.attendify.services.mailingservice.models.GenericEmail;
import ehb.attendify.services.mailingservice.models.mail.header.Recipient;
import ehb.attendify.services.mailingservice.services.api.EmailService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Log4j2
@Service
public class EmailServiceImpl implements EmailService {
    @Value("${sendgrid.api-key}")
    private String sendGridApiKey;

    @Value("${sendgrid.from-email}")
    private String fromEmail;

    @Override
    public void sendEmail(GenericEmail email) {
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
        Content emailContent = new Content("text/plain", email.getBody().getContent());
        mail.addContent(emailContent);

        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);

            log.info("SendGrid Response Code: {}", response.getStatusCode());
            log.info("SendGrid Response Body: {}", response.getBody());
            log.info("SendGrid Response Headers: {}", response.getHeaders());

        } catch (IOException ex) {
            log.error("Error sending email via SendGrid", ex);
        }
    }
}
