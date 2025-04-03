package ehb.attendify.services.mailingservice.models.general;

import ehb.attendify.services.mailingservice.models.enums.Operation;
import ehb.attendify.services.mailingservice.models.enums.Sender;
import lombok.Data;

@Data
public class Info {

    private Sender sender;

    private Operation operation;


}
