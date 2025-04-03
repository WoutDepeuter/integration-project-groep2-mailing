package ehb.attendify.services.mailingservice.services;

import ehb.attendify.services.mailingservice.dto.TemplateDto;
import ehb.attendify.services.mailingservice.models.template.Template;
import ehb.attendify.services.mailingservice.repositories.TemplateRepository;
import ehb.attendify.services.mailingservice.services.api.StringService;
import ehb.attendify.services.mailingservice.services.api.TemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TemplateServiceImpl implements TemplateService {

    private final TemplateRepository templateRepository;
    private final StringService stringService;


    @Override
    public Pair<String, Boolean> updateTemplate(TemplateDto dto) {
        Optional<Template> optionalTemplate = this.getTemplate(dto.getExchange(), dto.getRoutingKey());
        if (optionalTemplate.isPresent() && optionalTemplate.get().getVersion().equals(dto.getVersion())) {
            return Pair.of("", false);
        }

        Template template = optionalTemplate.orElse(
                Template.builder()
                        .exchange(dto.getExchange())
                        .routingKey(dto.getRoutingKey())
                        .build());

        var oldVersion = template.getVersion();

        template.setTemplate(this.stringService.toByteArray(dto.getTemplate()));
        template.setVersion(dto.getVersion());
        template.setDisplayName(dto.getDisplayName());
        template.setSubject(dto.getSubject());
        template.setContentType(dto.getContentType());

        templateRepository.save(template);
        return Pair.of(oldVersion, true);
    }

    @Override
    public Optional<Template> getTemplate(String exchange, String routingKey) {
        return this.templateRepository.getFirstByExchangeAndRoutingKey(exchange, routingKey);
    }
}
