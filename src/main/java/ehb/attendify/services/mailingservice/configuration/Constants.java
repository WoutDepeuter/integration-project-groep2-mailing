package ehb.attendify.services.mailingservice.configuration;

import ehb.attendify.services.mailingservice.models.enums.Sender;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
public class Constants {

    public static final String EVENT_REGISTER = "event.register";
    public static final String EVENT_UNREGISTER = "event.unregister";
    public static final String EVENT_CREATE = "event.create";
    public static final String EVENT_UPDATE = "event.update";
    public static final String EVENT_CANCEL = "event.cancel";
    public static final String EVENT_MAILING_LIST = "event.mailingList";

    public static final String SESSION_REGISTER = "session.register";
    public static final String SESSION_UNREGISTER = "session.unregister";
    public static final String SESSION_CREATE = "session.create";
    public static final String SESSION_UPDATE = "session.update";
    public static final String SESSION_CANCEL = "session.cancel";
    public static final String SESSION_DELAY = "session.delay";

    public static final String INVOICE_SEND = "invoice.send";

}
