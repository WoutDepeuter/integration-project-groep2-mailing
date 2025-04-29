package ehb.attendify.services.mailingservice.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import ehb.attendify.services.mailingservice.exceptions.InvalidUserLocation;
import ehb.attendify.services.mailingservice.models.mail.header.Header;
import ehb.attendify.services.mailingservice.models.mail.header.Recipient;
import ehb.attendify.services.mailingservice.models.template.Template;
import ehb.attendify.services.mailingservice.services.api.MessageMapperService;
import ehb.attendify.services.mailingservice.exceptions.UnknownMessageSource;
import ehb.attendify.services.mailingservice.services.api.UserService;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class MessageMapperServiceImpl implements MessageMapperService {

    private final XmlMapper xmlMapper;
    private final UserService userService;

    @Override
    @Timed(description = "Time spend converting unknown messages to their Java objects")
    @SneakyThrows
    public JsonNode map(Message message) throws UnknownMessageSource {
        return xmlMapper.readTree(message.getBody());
    }

    @Override
    @Timed(description = "Time spend extracting mail headers")
    public Header extractHeader(Template template, JsonNode obj) throws InvalidUserLocation {
        var steps = template.getUserLocation().split("\\.");
        if (steps.length == 0) {
            log.warn("Template user location has no steps, cannot build header");
            throw new InvalidUserLocation();
        }

        JsonNode userLoc = obj;
        for (int i = 0; i < steps.length; i++) {
            if (!userLoc.has(steps[i])) {
                log.error("Invalid step {} #{} in {}, for {}",
                        steps[i], i, template.getUserLocation(), obj.toString());
                throw new InvalidUserLocation();
            }
            userLoc = userLoc.get(steps[i]);
        }

        List<Recipient> recipients = new ArrayList<>();
        switch (template.getUserLocationType()) {
            case SINGLE -> recipients.add(this.extractSingle(userLoc));
            case ARRAY -> {
                if (!userLoc.isArray()) {
                    log.error("User location was not an array, cannot find users {} @ {}", userLoc.toString(), template.getUserLocation());
                    throw new InvalidUserLocation();
                }
                for (var node : userLoc) {
                    recipients.add(this.extractSingle(node));
                }
            }
        }


        return Header.builder()
                .recipients(recipients)
                .subject(template.getSubject())
                .build();
    }

    private Recipient extractSingle(JsonNode node) throws InvalidUserLocation {
        if (!node.isTextual()) {
            log.error("User location was not an string, cannot find user {}", node.toString());
            throw new InvalidUserLocation();
        }

        var userId = node.asText();
        return Recipient.builder().email(this.userService.getEmail(userId).orElse(userId)).build();
    }
}
