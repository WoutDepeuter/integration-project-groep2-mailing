package ehb.attendify.services.mailingservice.services;

import ehb.attendify.services.mailingservice.configuration.Constants;
import ehb.attendify.services.mailingservice.models.general.AttendifyUserMessage;
import ehb.attendify.services.mailingservice.services.api.MessageMapperService;
import ehb.attendify.services.mailingservice.services.api.UnknownMessageSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.support.converter.Jackson2XmlMessageConverter;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class MessageMapperServiceImpl implements MessageMapperService {

    private final Jackson2XmlMessageConverter xmlMessageConverter;

    @Override
    public Object map(Message message) throws UnknownMessageSource {
        var exchange = message.getMessageProperties().getReceivedExchange();
        var routingKey = message.getMessageProperties().getReceivedRoutingKey();

        return switch (exchange) {
            case "user":
                yield this.userMapper(message);

            default:
                throw new UnknownMessageSource(exchange, routingKey);
        };

    }

    private Object userMapper(Message message) {
        var exchange = message.getMessageProperties().getReceivedExchange();
        var routingKey = message.getMessageProperties().getReceivedRoutingKey();

        return switch (routingKey) {
            case Constants.USER_PASSWORD_GENERATED:
                yield xmlMessageConverter.fromMessage(message, AttendifyUserMessage.class);
            default:
                throw new UnknownMessageSource(exchange, routingKey);
        };
    }
}
