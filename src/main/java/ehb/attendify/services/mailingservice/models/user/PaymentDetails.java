package ehb.attendify.services.mailingservice.models.user;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

@Data
public class PaymentDetails {

    @JacksonXmlProperty(localName = "facturation_address")
    private FacturationAddress facturationAddress;

    @JacksonXmlProperty(localName = "payment_method")
    private String paymentMethod;

    @JacksonXmlProperty(localName = "card_number")
    private String cardNumber;

}