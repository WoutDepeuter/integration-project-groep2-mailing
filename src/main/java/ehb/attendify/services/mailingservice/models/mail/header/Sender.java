package ehb.attendify.services.mailingservice.models.mail.header;

import jakarta.annotation.Nullable;
import lombok.Data;

@Data
public class Sender {

    @Nullable
    private Long userId;

    private String name;

    private String email;

}
