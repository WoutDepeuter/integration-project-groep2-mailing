package ehb.attendify.services.mailingservice.models.mail.body;

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
