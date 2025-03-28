package ehb.attendify.services.mailingservice.models.mail.header;

import lombok.Data;

import java.util.List;

@Data
public class Header {

    private Sender sender;

    private List<Recipient> recipients;

    private List<Recipient> cc;

    private List<Recipient> bcc;

    private String subject;

}
