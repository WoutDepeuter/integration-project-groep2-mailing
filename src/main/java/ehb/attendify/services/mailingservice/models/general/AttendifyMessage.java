package ehb.attendify.services.mailingservice.models.general;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import ehb.attendify.services.mailingservice.models.user.Company;
import ehb.attendify.services.mailingservice.models.user.User;
import lombok.Data;

@Data
@JacksonXmlRootElement(namespace = "attendify")
public class AttendifyMessage<T> {

    private Info info;

    @JacksonXmlProperty(localName = "payload_type")
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
    @JsonSubTypes({
            @JsonSubTypes.Type(value = User.class, name = "user"),
            @JsonSubTypes.Type(value = Company.class, name = "company")
    })
    private T payload;

}
