package ehb.attendify.services.mailingservice.models.mail.header;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Header {

    private Sender sender;

    private List<Recipient> recipients;

    private List<Recipient> cc;

    private List<Recipient> bcc;

    private String subject;

}
