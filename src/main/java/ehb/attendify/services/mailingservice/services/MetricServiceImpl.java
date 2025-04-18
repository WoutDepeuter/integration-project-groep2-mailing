package ehb.attendify.services.mailingservice.services;

import ehb.attendify.services.mailingservice.services.api.MetricService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.Getter;
import org.springframework.stereotype.Service;

@Service
@Getter
public class MetricServiceImpl implements MetricService {

    private final Counter mailCounter;
    private final Counter incomingMailCounter;
    private final MeterRegistry registry;

    public MetricServiceImpl(MeterRegistry registry) {
        this.registry = registry;

        this.mailCounter = Counter.builder("mails_send")
                .description("Total amount of mails send")
                .register(registry);

        this.incomingMailCounter = Counter.builder("incoming_mail")
                .description("Mail requests on mailing.mail")
                .register(registry);
    }

}
