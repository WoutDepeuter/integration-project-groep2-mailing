package ehb.attendify.services.mailingservice.models.mail.body;


import ehb.attendify.services.mailingservice.models.enums.ContentType;
import lombok.Data;

import java.util.List;

@Data
public class Body {

    private String content;

    private ContentType contentType = ContentType.TEXT_PLAIN;

    private List<Attachment> attachments;

}
