package ehb.attendify.services.mailingservice.dto;

import ehb.attendify.services.mailingservice.models.enums.ContentType;
import lombok.Data;

@Data
public class TemplateDto {

    private String exchange;

    private String routingKey;

    private String version;

    private String displayName;

    private ContentType contentType;

    private String subject;

    private String template;

}
