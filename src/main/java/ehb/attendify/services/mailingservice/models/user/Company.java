package ehb.attendify.services.mailingservice.models.user;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

@Data
public class Company {

    private String id;

    private String name;

    @JacksonXmlProperty(localName = "VAT_number")
    private String vatNumber;

    private Address address;

}
