package ehb.attendify.services.mailingservice.heartbeat;

import ehb.attendify.services.mailingservice.dto.HeartBeatDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Log4j2
@RequiredArgsConstructor
@Component
public class HeartBeat {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    private Queue monitoringHeartbeatQueue;

    @Autowired
    private DataSource dataSource;

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

        String queueName = monitoringHeartbeatQueue.getName();

        HeartBeatDTO dto = new HeartBeatDTO("mailing-service", "mailing-container", System.currentTimeMillis());
        HeartBeatToXml xml = HeartBeatDTO.toXml(dto);
        log.debug("Sending heartbeat to {} queue", queueName);

        rabbitTemplate.convertAndSend(queueName, xml);
    }
}

