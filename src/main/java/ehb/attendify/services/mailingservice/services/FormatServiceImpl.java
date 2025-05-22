package ehb.attendify.services.mailingservice.services;

import com.fasterxml.jackson.databind.JsonNode;
import ehb.attendify.services.mailingservice.exceptions.InvalidUserLocation;
import ehb.attendify.services.mailingservice.models.GenericEmail;
import ehb.attendify.services.mailingservice.models.mail.body.Body;
import ehb.attendify.services.mailingservice.models.template.Template;
import ehb.attendify.services.mailingservice.services.api.FormatService;
import ehb.attendify.services.mailingservice.services.api.MessageMapperService;
import ehb.attendify.services.mailingservice.services.api.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@Service
@RequiredArgsConstructor
public class FormatServiceImpl implements FormatService {

    private final UserService userService;
    private final MessageMapperService messageMapperService;
    private final TemplateEngine templateEngine;

    @Override
    public GenericEmail formatEmail(Template template, JsonNode data) throws InvalidUserLocation {
        var header = this.messageMapperService.extractHeader(template, data);
        if (header == null) {
            return null;
        }

        return GenericEmail.builder()
                .header(header)
                .body(Body.builder()
                        .contentType(template.getContentType())
                        .content(this.format(template, data))
                        .build())
                .build();
    }

    @Override
    public String format(Template template, JsonNode data) {
        Context ctx = new Context();
        ctx.setVariable("data", data);
        ctx.setVariable("formatter", (FormatService)this);

        var nativeData = this.extractNode(data);
        if (nativeData instanceof Map ) {
            ctx.setVariables((Map<String, Object>)nativeData);
        } else {
            log.error("Failed to map to a proper map");
        }

        return this.templateEngine.process(template.getTemplate(), ctx);
    }

    public Object extractNode(JsonNode node) {
        if (node.isObject()) {
            Map<String, Object> map = new HashMap<>();
            for (var it = node.fields(); it.hasNext(); ) {
                var e = it.next();
                var key = e.getKey();
                var value = e.getValue();

                map.put(key, this.extractNode(value));
            }
            return map;
        }

        if (node.isArray()) {
            List<Object> list = new ArrayList<>();
            for (var it = node.elements(); it.hasNext(); ) {
                var listNode = it.next();
                list.add(this.extractNode(listNode));
            }
            return list;
        }

        return node.asText();
    }


    @Override
    public String formatUserName(String userId, boolean title) {
        var opt = this.userService.getPreferencesForUser(userId);
        if (opt.isEmpty()) {
            return userId;
        }

        var mailUser = opt.get();
        StringBuilder builder = new StringBuilder();

        if (title && !mailUser.getMailGreetingType().isEmpty()) {
            builder.append(mailUser.getMailGreetingType());
            builder.append(" ");
        }

        builder.append(mailUser.getLastName());
        builder.append(", ");
        builder.append(mailUser.getFirstName());
        return builder.toString();
    }
}
