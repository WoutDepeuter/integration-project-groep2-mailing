package ehb.attendify.services.mailingservice.heartbeat;

import ehb.attendify.services.mailingservice.dto.HeartBeatDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Log4j2
@Component
@RequiredArgsConstructor
public class HeartBeat {

    private final DataSource dataSource;
    private final RabbitTemplate rabbitTemplate;
    private final Queue monitoringHeartbeatQueue;

    private long lastBeat = -1;

    public boolean isDatabaseAlive() {
        try (Connection connection = dataSource.getConnection()) {
            return connection.isValid(5);
        } catch (SQLException e) {
            return false;
        }
    }

    @Scheduled(fixedRate = 5000)
    public void sendHeartbeat() {
        if (!isDatabaseAlive()) {
            log.warn("database is not alive, skipping sending heartbeat");
            return;
        }

        long now = System.currentTimeMillis();
        if (lastBeat == -1) {
            lastBeat = now;
        }
        log.debug("Sending heartbeat, last beat {}ms ago", now - lastBeat);
        lastBeat = now;

        this.rabbitTemplate.convertAndSend(this.monitoringHeartbeatQueue.getName(), new HeartBeatDTO("mailing-service", now));
    }
}

