package ehb.attendify.services.mailingservice.deserializers;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import ehb.attendify.services.mailingservice.models.template.Template;

import java.io.IOException;

public class UserLocationTypeDeserializer extends JsonDeserializer<Template.UserLocationType> {
    @Override
    public Template.UserLocationType deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        String value = p.getText();
        int intValue = p.getIntValue();

        if (value == null) {
            return null;
        }

        for (Template.UserLocationType locationType : Template.UserLocationType.values()) {
            if (locationType.name().equalsIgnoreCase(value)) {
                return locationType;
            }

            if (locationType.ordinal() == intValue) {
                return locationType;
            }

        }

        return null;
    }
}
