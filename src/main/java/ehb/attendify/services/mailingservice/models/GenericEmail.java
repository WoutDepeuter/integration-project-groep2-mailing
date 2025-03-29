package ehb.attendify.services.mailingservice.models;

import ehb.attendify.services.mailingservice.models.mail.body.Body;
import ehb.attendify.services.mailingservice.models.mail.header.Header;
import lombok.Data;

@Data
public class GenericEmail {

    private Header header;

    private Body body;

    private String name;


}
