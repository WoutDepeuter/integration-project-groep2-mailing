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

    public MetricServiceImpl(MeterRegistry registry) {
        this.mailCounter = Counter.builder("mails_send")
                .description("Total amount of mails send")
                .register(registry);
    }

}
