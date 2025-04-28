package ehb.attendify.services.mailingservice.deserializers;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import ehb.attendify.services.mailingservice.models.enums.Sender;

import java.io.IOException;

public class SenderDeserializer extends JsonDeserializer<Sender> {
    @Override
    public Sender deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        String value = p.getText();
        int intValue = p.getIntValue();

        if (value == null) {
            return null;
        }

        for (Sender sender : Sender.values()) {
            if (sender.name().equalsIgnoreCase(value)) {
                return sender;
            }

            if (sender.ordinal() == intValue) {
                return sender;
            }
        }

        return null;
    }
}
