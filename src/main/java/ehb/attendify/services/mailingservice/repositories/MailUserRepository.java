package ehb.attendify.services.mailingservice.repositories;

import ehb.attendify.services.mailingservice.models.MailUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MailUserRepository extends JpaRepository<MailUser, Long> {

    Optional<MailUser> findByUserId(String userId);

    Optional<MailUser> findByEmail(String email);

}
