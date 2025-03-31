package ehb.attendify.services.mailingservice.models.user;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

@Data
public class FacturationAddress {

    private String street;

    private int number;

    @JacksonXmlProperty(localName = "company_bus_number")
    private String companyBusNumber;

    private String city;

    private String province;

    private String country;

    @JacksonXmlProperty(localName = "postal_code")
    private String postalCode;

}
