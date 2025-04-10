package ehb.attendify.services.mailingservice.services.api;

import org.springframework.amqp.core.Message;

public interface MessageMapperService {

    Object map(Message message) throws UnknownMessageSource;

}
