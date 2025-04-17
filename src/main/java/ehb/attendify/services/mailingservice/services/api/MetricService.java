package ehb.attendify.services.mailingservice.services.api;

import io.micrometer.core.instrument.Counter;

public interface MetricService {

    Counter getMailCounter();

}
