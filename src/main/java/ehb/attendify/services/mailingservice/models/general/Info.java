package ehb.attendify.services.mailingservice.models.general;

import ehb.attendify.services.mailingservice.models.enums.Operation;
import lombok.Data;

@Data
public class Info {

    private String sender;

    private Operation operation;


}
