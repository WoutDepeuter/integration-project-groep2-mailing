package ehb.attendify.services.mailingservice.services.api;

import ehb.attendify.services.mailingservice.dto.TemplateDto;
import ehb.attendify.services.mailingservice.models.template.Template;

import java.util.Optional;

public interface TemplateService {

    TemplateUpdateResponse updateTemplate(TemplateDto templateDto);

    Optional<Template> getTemplate(String exchange, String routingKey);

}
