package ehb.attendify.services.mailingservice.services;

import ehb.attendify.services.mailingservice.dto.UserMailPreferencesDto;
import ehb.attendify.services.mailingservice.models.GenericEmail;
import ehb.attendify.services.mailingservice.models.UserMailPreferences;
import ehb.attendify.services.mailingservice.models.mail.body.Body;
import ehb.attendify.services.mailingservice.models.mail.header.Header;
import ehb.attendify.services.mailingservice.models.mail.header.Recipient;
import ehb.attendify.services.mailingservice.repositories.UserMailPreferencesRepository;
import ehb.attendify.services.mailingservice.services.api.EmailService;
import ehb.attendify.services.mailingservice.services.api.UserMailPreferencesService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserMailPreferencesServiceImpl implements UserMailPreferencesService {

    private final UserMailPreferencesRepository userMailPreferencesRepository;
    private final ModelMapper mapper;
    private final EmailService emailService;

    @Override
    public Optional<UserMailPreferences> getPreferencesForUser(Long userId) {
        return this.userMailPreferencesRepository.findByUserId(userId);
    }

    @Override
    public List<UserMailPreferences> getAllPreferences() {
        return this.userMailPreferencesRepository.findAll();
    }


    @Override
    public UserMailPreferences updatePreferencesForUser(Long userId, UserMailPreferencesDto preferences) {
        var optionalCur = this.userMailPreferencesRepository.findByUserId(userId);
        UserMailPreferences updatedPref;

        if (optionalCur.isEmpty()) {
            updatedPref = this.mapper.map(preferences, UserMailPreferences.class);
            updatedPref.setUserId(userId);
        } else {
            updatedPref = optionalCur.get();
            if (preferences.getMailGreetingType() != null) {
                updatedPref.setMailGreetingType(preferences.getMailGreetingType());
            }
        }

        updatedPref = this.userMailPreferencesRepository.save(updatedPref);

        Header header = new Header();
        // example of two recipients with one cc
        Recipient recipient = new Recipient();
        recipient.setEmail("tristanvong1@gmail.com");

        Recipient recipient2 = new Recipient();
        recipient2.setEmail("tristanvong.ehb@gmail.com");

        Recipient ccRecipient = new Recipient();
        ccRecipient.setEmail("test@gmail.com");

        header.setRecipients(List.of(recipient, recipient2));
        header.setCc(List.of(ccRecipient));
        header.setSubject("Mail Preferences Updated");

        Body body = new Body();
        body.setContent("Your mail preferences have been updated to: " + updatedPref.getMailGreetingType());

        GenericEmail email = new GenericEmail();
        email.setHeader(header);
        email.setBody(body);

        emailService.sendEmail(email);

        return updatedPref;
    }
}