package ehb.attendify.services.mailingservice.models.user;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

@Data
@JacksonXmlRootElement(namespace = "user", localName = "user")
public class User {

    private String uid;

    @JacksonXmlProperty(localName = "first_name")
    private String firstName;

    @JacksonXmlProperty(localName = "last_name")
    private String lastName;

    @JacksonXmlProperty(localName = "date_of_birth")
    private String dateOfBirth;

    @JacksonXmlProperty(localName = "phone_number")
    private String phoneNumber;

    private String title;

    private String email;

    private String password;

    private Address address;

    @JacksonXmlProperty(localName = "payment_details")
    private PaymentDetails paymentDetails;

    private Company company;

    @JacksonXmlProperty(localName = "from_company")
    private boolean fromCompany;

}
