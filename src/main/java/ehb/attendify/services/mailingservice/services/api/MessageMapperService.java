package ehb.attendify.services.mailingservice.services.api;

import com.fasterxml.jackson.databind.JsonNode;
import ehb.attendify.services.mailingservice.exceptions.InvalidUserLocation;
import ehb.attendify.services.mailingservice.exceptions.UnknownMessageSource;
import ehb.attendify.services.mailingservice.models.mail.header.Header;
import ehb.attendify.services.mailingservice.models.template.Template;
import org.springframework.amqp.core.Message;

public interface MessageMapperService {

    JsonNode map(Message message) throws UnknownMessageSource;

    Header extractHeader(Template template, JsonNode obj) throws InvalidUserLocation;

}
