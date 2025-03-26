package ehb.attendify.services.mailingservice.models.mail.header;

import lombok.Data;

@Data
public class Recipient {

    private Long userId;

    private String name;

    private String email;

}
