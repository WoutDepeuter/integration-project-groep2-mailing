package ehb.attendify.services.mailingservice.models.mail.body;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class Body {

    @Getter
    @Setter
    private String content;

    private List<Attachment> attachments;

}
