package ehb.attendify.services.mailingservice.models;

import ehb.attendify.services.mailingservice.models.enums.MailGreetingType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class UserMailPreferences {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private Long userId;

    private MailGreetingType mailGreetingType;

}
