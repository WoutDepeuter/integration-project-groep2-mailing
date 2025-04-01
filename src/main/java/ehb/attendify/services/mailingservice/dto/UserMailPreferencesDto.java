package ehb.attendify.services.mailingservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserMailPreferencesDto {

    private String mailGreetingType;

}
