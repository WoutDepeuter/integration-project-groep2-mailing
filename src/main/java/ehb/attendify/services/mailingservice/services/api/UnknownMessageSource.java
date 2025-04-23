package ehb.attendify.services.mailingservice.services.api;

public class UnknownMessageSource extends RuntimeException {
    public UnknownMessageSource(String exchange, String routingKey) {
        super("Received an unknown message type on " + exchange + " via " + routingKey);
    }
}
