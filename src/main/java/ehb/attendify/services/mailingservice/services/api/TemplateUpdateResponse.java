package ehb.attendify.services.mailingservice.services.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TemplateUpdateResponse {

    private final boolean hasUpdated;
    private final Integer updatedFrom;
    private final Integer updatedTo;

}
