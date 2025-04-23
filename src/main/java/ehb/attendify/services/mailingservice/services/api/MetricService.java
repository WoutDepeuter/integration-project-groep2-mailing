package ehb.attendify.services.mailingservice.services.api;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

public interface MetricService {

    Counter getMailCounter();

    Counter getIncomingMailCounter();

    MeterRegistry getRegistry();

}
