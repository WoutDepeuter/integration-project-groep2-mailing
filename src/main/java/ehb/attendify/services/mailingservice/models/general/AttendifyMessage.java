package ehb.attendify.services.mailingservice.models.general;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

@Data
@JacksonXmlRootElement(namespace = "attendify")
public class AttendifyMessage<T> {

    private Info info;

    private T payload;

}
