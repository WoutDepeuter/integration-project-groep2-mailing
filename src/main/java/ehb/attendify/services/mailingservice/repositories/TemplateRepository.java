package ehb.attendify.services.mailingservice.repositories;

import ehb.attendify.services.mailingservice.models.template.Template;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TemplateRepository extends JpaRepository<Template, Long> {

    Optional<Template> getFirstByExchangeAndRoutingKey(String exchange, String routingKey);

}
