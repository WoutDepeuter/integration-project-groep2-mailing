package ehb.attendify.services.mailingservice.models.mail.body;


import ehb.attendify.services.mailingservice.models.enums.ContentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Body {

    private String content;

    private ContentType contentType = ContentType.TEXT_PLAIN;

    private List<Attachment> attachments;

}
