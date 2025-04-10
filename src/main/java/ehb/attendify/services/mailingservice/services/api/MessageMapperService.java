package ehb.attendify.services.mailingservice.services.api;

import ehb.attendify.services.mailingservice.models.mail.header.Header;
import ehb.attendify.services.mailingservice.models.template.Template;
import org.springframework.amqp.core.Message;

public interface MessageMapperService {

    Object map(Message message) throws UnknownMessageSource;

    Header extractHeader(Template template, Object obj);

}
