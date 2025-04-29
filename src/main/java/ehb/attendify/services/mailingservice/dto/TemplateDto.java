package ehb.attendify.services.mailingservice.dto;

import ehb.attendify.services.mailingservice.models.enums.ContentType;
import ehb.attendify.services.mailingservice.models.template.UserLocationType;
import lombok.Data;

@Data
public class TemplateDto {

    private String exchange;

    private String routingKey;

    private Integer version;

    private String displayName;

    private ContentType contentType;

    private String subject;

    private String template;

    private String userLocation;

    private UserLocationType userLocationType;

}
