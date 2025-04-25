package ehb.attendify.services.mailingservice.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JacksonXmlRootElement(localName = "heartbeat")
public class HeartBeatDTO {

    @JacksonXmlProperty(localName = "sender")
    private final String sender;

    @JacksonXmlProperty(localName = "timestamp")
    private final long timestamp;
}
