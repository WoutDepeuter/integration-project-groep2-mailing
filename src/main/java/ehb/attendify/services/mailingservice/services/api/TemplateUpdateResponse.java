package ehb.attendify.services.mailingservice.services.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TemplateUpdateResponse {

    private final boolean hasUpdated;
    private final String updatedFrom;
    private final String updatedTo;

}
