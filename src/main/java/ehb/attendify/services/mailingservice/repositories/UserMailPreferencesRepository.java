package ehb.attendify.services.mailingservice.repositories;

import ehb.attendify.services.mailingservice.models.UserMailPreferences;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserMailPreferencesRepository extends JpaRepository<UserMailPreferences, Long> {

    Optional<UserMailPreferences> findByUserId(Long userId);

}
