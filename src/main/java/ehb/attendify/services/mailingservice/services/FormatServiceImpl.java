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
        this.insertJsonNode(ctx, data);
        ctx.setVariable("formatter", (FormatService)this);
        return this.templateEngine.process(template.getTemplate(), ctx);
    }

    private void insertJsonNode(Context ctx, JsonNode node) {
        if (node != null && node.isObject()) {
            Map<String, Object> root = new HashMap<>();
            populateMapFromJsonNode(root, node);
            for (Map.Entry<String, Object> entry : root.entrySet()) {
                ctx.setVariable(entry.getKey(), entry.getValue());
            }
        }
    }

    private void populateMapFromJsonNode(Map<String, Object> map, JsonNode node) {
        node.fields().forEachRemaining(entry -> {
            JsonNode value = entry.getValue();
            if (value.isObject()) {
                Map<String, Object> childMap = new HashMap<>();
                populateMapFromJsonNode(childMap, value);
                map.put(entry.getKey(), childMap);
            } else if (value.isArray()) {
                List<Object> list = new ArrayList<>();
                for (JsonNode item : value) {
                    if (item.isObject()) {
                        Map<String, Object> itemMap = new HashMap<>();
                        populateMapFromJsonNode(itemMap, item);
                        list.add(itemMap);
                    } else {
                        list.add(getJsonValue(item));
                    }
                }
                map.put(entry.getKey(), list);
            } else {
                map.put(entry.getKey(), getJsonValue(value));
            }
        });
    }

    private Object getJsonValue(JsonNode node) {
        if (node.isTextual()) return node.textValue();
        if (node.isNumber()) return node.numberValue();
        if (node.isBoolean()) return node.booleanValue();
        if (node.isNull()) return null;
        return node.toString();
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
