package ehb.attendify.services.mailingservice.models.enums;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import ehb.attendify.services.mailingservice.deserializers.SenderDeserializer;

@JsonDeserialize(using = SenderDeserializer.class)
public enum Sender {
    CRM,
    POS
}
