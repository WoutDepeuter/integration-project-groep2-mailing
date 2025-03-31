package ehb.attendify.services.mailingservice.models.user;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

@Data
@JacksonXmlRootElement(localName = "address")
public class Address {

    private String street;

    private int number;

    @JacksonXmlProperty(localName = "bus_number")
    private String busNumber;

    private String city;

    private String province;

    private String country;

    @JacksonXmlProperty(localName = "postal_code")
    private String postalCode;

}
