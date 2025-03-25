package ehb.attendify.services.mailingservice.services;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EmailService {
    @Value("${sendgrid.api-key}")
    private String sendGridApiKey;
    @Value("${sendgrid.from-email}")
    private String fromEmail;

    public void sendEmail(String toEmail, String subject, String body) {
        Email from = new Email(fromEmail);
        Email to = new Email(toEmail);
        Content emailContent = new Content("text/plain", body);
        Mail mail = new Mail(from, subject, to, emailContent);

        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);

            System.out.println("SendGrid Response Code: " + response.getStatusCode());
            System.out.println("SendGrid Response Body: " + response.getBody());
            System.out.println("SendGrid Response Headers: " + response.getHeaders());

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
