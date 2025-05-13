package ehb.attendify.services.mailingservice.services;

import ehb.attendify.services.mailingservice.dto.TemplateDto;
import ehb.attendify.services.mailingservice.models.template.Template;
import ehb.attendify.services.mailingservice.repositories.TemplateRepository;
import ehb.attendify.services.mailingservice.services.api.TemplateService;
import ehb.attendify.services.mailingservice.services.api.TemplateUpdateResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.HashMap;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class TemplateServiceImpl implements TemplateService {

    private final TemplateRepository templateRepository;
    private final TemplateEngine templateEngine;


    @Override
    public TemplateUpdateResponse updateTemplate(TemplateDto dto) {
        if (!this.validTemplate(dto.getTemplate())) {
            return new TemplateUpdateResponse(false, -1, -1);
        }

        Optional<Template> optionalTemplate = this.getTemplate(dto.getExchange(), dto.getRoutingKey());

        if (optionalTemplate.isPresent() && optionalTemplate.get().getVersion() >= dto.getVersion()) {
            return new TemplateUpdateResponse(false, optionalTemplate.get().getVersion(), dto.getVersion());
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

    private boolean validTemplate(String template) {
        Context context = new Context();
        context.setVariable("data", new HashMap<>());

        try {
            this.templateEngine.process(template, context);
        } catch (Exception e) {
            log.error("Invalid template passed", e);
            return false;
        }

        return true;
    }
}
