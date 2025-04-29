package ehb.attendify.services.mailingservice.deserializers;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import ehb.attendify.services.mailingservice.models.template.UserLocationType;

import java.io.IOException;

public class UserLocationTypeDeserializer extends JsonDeserializer<UserLocationType> {
    @Override
    public UserLocationType deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        String value = p.getText();

        if (value == null) {
            return null;
        }

        for (UserLocationType locationType : UserLocationType.values()) {
            if (locationType.name().equalsIgnoreCase(value)) {
                return locationType;
            }
        }

        int intValue = p.getIntValue();
        for (UserLocationType locationType : UserLocationType.values()) {
            if (locationType.ordinal() == intValue) {
                return locationType;
            }
        }

        throw new IllegalArgumentException("Invalid value for UserLocationType: " + value);
    }
}
