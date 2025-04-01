package ehb.attendify.services.mailingservice.models.enums;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import ehb.attendify.services.mailingservice.deserializers.OperationDeserializer;

@JsonDeserialize(using = OperationDeserializer.class)
public enum Operation {
    CREATE,
    UPDATE,
    DELETE,
}
