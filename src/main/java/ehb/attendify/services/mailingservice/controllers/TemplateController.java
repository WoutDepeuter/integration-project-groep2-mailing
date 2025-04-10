package ehb.attendify.services.mailingservice.controllers;

import ehb.attendify.services.mailingservice.dto.TemplateDto;
import ehb.attendify.services.mailingservice.services.api.TemplateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@RequiredArgsConstructor
public class TemplateController {

    private final TemplateService templateService;


    @RabbitListener(queues = "#{templateQueue.name}")
    public void onTemplateChange(TemplateDto template) {
        var res = this.templateService.updateTemplate(template);
        if (!res.isHasUpdated()) {
            log.debug("Template {} was not updated; version {} was up to date", template.getDisplayName(), template.getVersion());
            return;
        }

        log.info("Updated template {} from version {} to {}", template.getDisplayName(), res.getUpdatedFrom(), res.getUpdatedTo());
    }

}
