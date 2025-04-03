package ehb.attendify.services.mailingservice.models.general;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import ehb.attendify.services.mailingservice.models.user.User;
import lombok.Data;

@Data
@JacksonXmlRootElement(namespace = "attendify")
public class AttendifyMessage<T> {

    private Info info;

    private User user;

}
