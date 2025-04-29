package ehb.attendify.services.mailingservice.controllers;

import ehb.attendify.services.mailingservice.dto.TemplateDto;
import ehb.attendify.services.mailingservice.models.template.Template;
import ehb.attendify.services.mailingservice.services.api.TemplateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Log4j2
@Component
@RestController
@RequiredArgsConstructor
@RequestMapping("/Templating")
public class TemplateController {

    private final TemplateService templateService;
    private final ModelMapper mapper;


    @RabbitListener(queues = "#{templateQueue.name}")
    public void onTemplateChange(TemplateDto template) {
        this.update(template);
    }

    @GetMapping("/{exchange}/{routingKey}")
    public Optional<TemplateDto> getTemplate(@PathVariable String exchange, @PathVariable String routingKey) {
        if (exchange.isBlank() || routingKey.isBlank()) {
            throw new IllegalArgumentException("Exchange and routingKey must be non blank");
        }


        var template = this.templateService.getTemplate(exchange, routingKey);
        return template.map(value -> this.mapper.map(value, TemplateDto.class));
    }

    @PostMapping
    public void updateTemplate(@RequestBody TemplateDto template) {
        this.update(template);
    }

    private void update(TemplateDto template) {
        var res = this.templateService.updateTemplate(template);
        if (!res.isHasUpdated()) {
            log.debug("Template {} was not updated; version {} was up to date, or older. Version in db {}",
                    template.getDisplayName(), res.getUpdatedTo(), res.getUpdatedFrom());
            return;
        }

        log.info("Updated template {} from version {} to {}", template.getDisplayName(), res.getUpdatedFrom(), res.getUpdatedTo());
    }

}
