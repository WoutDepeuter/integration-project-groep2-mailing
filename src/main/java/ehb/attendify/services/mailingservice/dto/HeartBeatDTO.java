package ehb.attendify.services.mailingservice.dto;

import ehb.attendify.services.mailingservice.heartbeat.HeartBeatToXml;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HeartBeatDTO {

    private final String sender;

    private final String containerName;

    private final long timestamp;

    public static HeartBeatToXml toXml(HeartBeatDTO dto) {
        return new HeartBeatToXml(
                dto.getSender(),
                dto.getContainerName(),
                dto.getTimestamp()
        );
    }
}
