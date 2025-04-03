package ehb.attendify.services.mailingservice.services;

import ehb.attendify.services.mailingservice.models.GenericEmail;
import ehb.attendify.services.mailingservice.models.mail.body.Body;
import ehb.attendify.services.mailingservice.models.mail.header.Header;
import ehb.attendify.services.mailingservice.models.mail.header.Recipient;
import ehb.attendify.services.mailingservice.models.template.Template;
import ehb.attendify.services.mailingservice.models.user.User;
import ehb.attendify.services.mailingservice.services.api.FormatService;
import ehb.attendify.services.mailingservice.services.api.StringService;
import ehb.attendify.services.mailingservice.services.api.UserMailPreferencesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Tristan, extend this one when you make it with the engine. So you just have to overwrite {@link FormatService#format(Template, Object)}
 * And then comment @Service out here. And it should work I think? Otherwise, just modify and copy
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class DefaultFormatService implements FormatService {

    private final StringService stringService;
    private final UserMailPreferencesService userMailPreferencesService;

    @Override
    public GenericEmail formatSimpleEmail(Template template, User user, Object data) {
        return GenericEmail.builder()
                .header(Header.builder()
                        .recipients(List.of(
                                Recipient.builder()
                                        .userId(user.getId())
                                        .name(this.formatUserName(user))
                                        .email(user.getEmail())
                                        .build()
                        ))
                        .subject(template.getSubject())
                        .build())
                .body(Body.builder()
                        .contentType(template.getContentType())
                        .content(this.format(template, user))
                        .build())
                .build();
    }

    @Override
    public String format(Template template, Object data) {
        return this.stringService.fromByteArray(template.getTemplate());
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
