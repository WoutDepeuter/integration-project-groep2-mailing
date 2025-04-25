package ehb.attendify.services.mailingservice.heartbeat;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;

@JacksonXmlRootElement(localName = "attendify")
public class HeartBeatToXml {

    @JacksonXmlProperty(localName = "info")
    private Info info;

    public HeartBeatToXml(String sender, String containerName, long timestamp) {
        this.info = new Info(sender, containerName, timestamp);
    }

    @AllArgsConstructor
    public static class Info {
        @JacksonXmlProperty(localName = "sender")
        private String sender;

        @JacksonXmlProperty(localName = "container_name")
        private String containerName;

        @JacksonXmlProperty(localName = "timestamp")
        private long timestamp;
    }
}