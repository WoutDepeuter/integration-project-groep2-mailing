package ehb.attendify.services.mailingservice.services.api;

import ehb.attendify.services.mailingservice.dto.MailUserDto;
import ehb.attendify.services.mailingservice.models.MailUser;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Optional<String> getEmail(String userId);

    void setEmail(String userId, String email);

    Optional<MailUser> getPreferencesForUser(String userId);

    List<MailUser> getAllPreferences();

    MailUser updatePreferencesForUser(String userId, MailUserDto preferences);

}
