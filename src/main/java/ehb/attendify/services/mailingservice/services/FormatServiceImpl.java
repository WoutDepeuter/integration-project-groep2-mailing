package ehb.attendify.services.mailingservice.services;

import com.fasterxml.jackson.databind.JsonNode;
import ehb.attendify.services.mailingservice.exceptions.InvalidUserLocation;
import ehb.attendify.services.mailingservice.models.GenericEmail;
import ehb.attendify.services.mailingservice.models.mail.body.Body;
import ehb.attendify.services.mailingservice.models.mail.header.Header;
import ehb.attendify.services.mailingservice.models.mail.header.Recipient;
import ehb.attendify.services.mailingservice.models.template.Template;
import ehb.attendify.services.mailingservice.models.user.User;
import ehb.attendify.services.mailingservice.services.api.FormatService;
import ehb.attendify.services.mailingservice.services.api.MessageMapperService;
import ehb.attendify.services.mailingservice.services.api.UserMailPreferencesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class FormatServiceImpl implements FormatService {

    private final UserMailPreferencesService userMailPreferencesService;
    private final MessageMapperService messageMapperService;
    private final TemplateEngine templateEngine;

    @Override
    public GenericEmail formatEmail(Template template, JsonNode data) throws InvalidUserLocation {
        var header = this.messageMapperService.extractHeader(template, data);
        if (header == null) {
            return null;
        }

        return GenericEmail.builder()
                .header(header)
                .body(Body.builder()
                        .contentType(template.getContentType())
                        .content(this.format(template, data))
                        .build())
                .build();
    }

    @Override
    public String format(Template template, JsonNode data) {
        Context ctx = new Context();
        ctx.setVariable("data", data);
        return this.templateEngine.process(template.getTemplate(), ctx);
    }

    @Override
    public String formatUserName(User user, boolean title) {
        var optionalPref = this.userMailPreferencesService.getPreferencesForEmail(user.getEmail());
        if (optionalPref.isEmpty()) {
            log.error("No user preferences found for email {}", user.getEmail());
            return String.format("%s, %s", user.getLastName(), user.getFirstName());
        }

        var pref = optionalPref.get();
        StringBuilder builder = new StringBuilder();

        if (title && !pref.getMailGreetingType().isEmpty()) {
            builder.append(pref.getMailGreetingType());
            builder.append(" ");
        }

        builder.append(user.getLastName());
        builder.append(", ");
        builder.append(user.getFirstName());
        return builder.toString();
    }
}
