package ehb.attendify.services.mailingservice.services.api;

import ehb.attendify.services.mailingservice.dto.UserMailPreferencesDto;
import ehb.attendify.services.mailingservice.models.UserMailPreferences;

import java.util.List;
import java.util.Optional;

public interface UserMailPreferencesService {

    Optional<UserMailPreferences> getPreferencesForUser(Long userId);

    List<UserMailPreferences> getAllPreferences();

    UserMailPreferences updatePreferencesForUser(Long userId, UserMailPreferencesDto preferences);

    UserMailPreferences updatePreferencesForUserByEmail(String email, UserMailPreferencesDto preferencesDto);

}
