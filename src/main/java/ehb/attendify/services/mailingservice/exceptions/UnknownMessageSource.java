package ehb.attendify.services.mailingservice.exceptions;

public class UnknownMessageSource extends RuntimeException {
    public UnknownMessageSource(String exchange, String routingKey) {
        super("Received an unknown message type on " + exchange + " via " + routingKey);
    }
}
