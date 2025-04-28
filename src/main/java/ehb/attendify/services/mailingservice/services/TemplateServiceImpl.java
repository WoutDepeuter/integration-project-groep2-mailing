package ehb.attendify.services.mailingservice.services;

import ehb.attendify.services.mailingservice.dto.TemplateDto;
import ehb.attendify.services.mailingservice.models.template.Template;
import ehb.attendify.services.mailingservice.repositories.TemplateRepository;
import ehb.attendify.services.mailingservice.services.api.TemplateService;
import ehb.attendify.services.mailingservice.services.api.TemplateUpdateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TemplateServiceImpl implements TemplateService {

    private final TemplateRepository templateRepository;


    @Override
    public TemplateUpdateResponse updateTemplate(TemplateDto dto) {
        Optional<Template> optionalTemplate = this.getTemplate(dto.getExchange(), dto.getRoutingKey());

        if (optionalTemplate.isPresent() && optionalTemplate.get().getVersion() <= dto.getVersion()) {
            return new TemplateUpdateResponse(false, dto.getVersion(), dto.getVersion());
        }

        Template template = optionalTemplate.orElse(
                Template.builder()
                        .exchange(dto.getExchange())
                        .routingKey(dto.getRoutingKey())
                        .build());

        var oldVersion = template.getVersion();

        template.setTemplate(dto.getTemplate());
        template.setVersion(dto.getVersion());
        template.setDisplayName(dto.getDisplayName());
        template.setSubject(dto.getSubject());
        template.setContentType(dto.getContentType());
        template.setUserLocation(dto.getUserLocation());
        template.setUserLocationType(dto.getUserLocationType());

        templateRepository.save(template);

        return new TemplateUpdateResponse(true, oldVersion, template.getVersion());
    }

    @Override
    public Optional<Template> getTemplate(String exchange, String routingKey) {
        return this.templateRepository.getFirstByExchangeAndRoutingKey(exchange, routingKey);
    }
}
