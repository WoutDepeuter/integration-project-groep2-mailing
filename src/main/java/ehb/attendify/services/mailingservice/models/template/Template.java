package ehb.attendify.services.mailingservice.models.template;

import ehb.attendify.services.mailingservice.models.enums.ContentType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Template {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String exchange;

    private String routingKey;

    private Integer version;

    private String displayName;

    private ContentType contentType;

    private String subject;

    @Lob
    private String template;

    private String userLocation;

    private UserLocationType userLocationType;

}
