package ehb.attendify.services.mailingservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MailUserDto {

    private String mailGreetingType;
    
    private String firstName;
    
    private String lastName;

}
