package ehb.attendify.services.mailingservice.deserializers;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import ehb.attendify.services.mailingservice.models.enums.Operation;

import java.io.IOException;

public class OperationDeserializer extends JsonDeserializer<Operation> {
    @Override
    public Operation deserialize(JsonParser p, DeserializationContext deserializationContext) throws IOException, JacksonException {
        String value = p.getText();
        if (value == null) {
            return null;
        }

        for (Operation operation : Operation.values()) {
            if (value.equalsIgnoreCase(operation.name())) {
                return operation;
            }
        }

        int intValue = p.getIntValue();
        for (Operation operation : Operation.values()) {
            if (operation.ordinal() == intValue) {
                return operation;
            }
        }

        throw new IllegalArgumentException("Invalid value for Operation: " + value);
    }
}
