package ehb.attendify.services.mailingservice.dto;

import ehb.attendify.services.mailingservice.models.enums.MailGreetingType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserMailPreferencesDto {

    private MailGreetingType mailGreetingType;

}
