package ehb.attendify.services.mailingservice.models.general;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import ehb.attendify.services.mailingservice.models.event.Event;
import lombok.Data;

@Data
@JacksonXmlRootElement(namespace = "attendify")
public class AttendifyEventMessage {

    private Info info;

    private Event event;

}
