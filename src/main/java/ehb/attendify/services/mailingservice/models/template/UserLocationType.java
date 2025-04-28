package ehb.attendify.services.mailingservice.models.template;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import ehb.attendify.services.mailingservice.deserializers.UserLocationTypeDeserializer;

@JsonDeserialize(using = UserLocationTypeDeserializer.class)
public enum UserLocationType {
    SINGLE,
    ARRAY,
}
