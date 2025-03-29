package ehb.attendify.services.mailingservice.models.mail.body;


import lombok.Data;

import java.util.List;

@Data
public class Body {

    private String content;

    private List<Attachment> attachments;

}
