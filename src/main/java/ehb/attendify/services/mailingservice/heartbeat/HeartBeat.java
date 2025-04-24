package ehb.attendify.services.mailingservice.heartbeat;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

@Log4j2
@Component
@RequiredArgsConstructor
public class HeartBeat {

    private final RabbitTemplate rabbitTemplate;

    private static final String QUEUE_NAME = "monitoring.heartbeat";

    @Scheduled(fixedRate = 5000)
    public void sendHeartbeat() {
        Instant now = Instant.now();
        String timestamp = DateTimeFormatter.ISO_INSTANT.format(now);
        String xml = String.format("""
                <attendify>
                    <info>
                        <sender>mailing</sender>
                        <container_name>mailing-container</container_name>
                        <timestamp>%s</timestamp>
                    </info>
                </attendify>
                """, timestamp);

        // handig om te weten maar spammed terminal dus maybe niet?
        log.info("Sending heartbeat to {} queue", QUEUE_NAME);
        rabbitTemplate.convertAndSend(QUEUE_NAME, xml);
    }
}
