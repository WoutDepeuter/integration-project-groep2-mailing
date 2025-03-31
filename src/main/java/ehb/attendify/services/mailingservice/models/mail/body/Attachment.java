package ehb.attendify.services.mailingservice.models.mail.body;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Attachment {

    private String fileName;

    /**
     * Base64 encoded file content
     */
    private String fileContent;

    /**
     * The filesize in bytes, to double-check fileContent after conversion.
     */
    private Integer fileSize;

}
