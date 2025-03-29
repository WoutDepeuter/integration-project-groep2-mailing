package ehb.attendify.services.mailingservice.services.api;

import ehb.attendify.services.mailingservice.models.GenericEmail;

public interface EmailService {
    void sendEmail(GenericEmail email);
}
