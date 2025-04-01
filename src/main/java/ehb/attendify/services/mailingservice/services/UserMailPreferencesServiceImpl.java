package ehb.attendify.services.mailingservice.services;

import ehb.attendify.services.mailingservice.dto.UserMailPreferencesDto;
import ehb.attendify.services.mailingservice.models.UserMailPreferences;
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

        return updatedPref;
    }

    @Override
    public UserMailPreferences updatePreferencesForUserByEmail(String email, UserMailPreferencesDto preferences) {
        var optionalCur = this.userMailPreferencesRepository.findByEmail(email);

        UserMailPreferences updatedPref;
        if (optionalCur.isEmpty()) {
            updatedPref = this.mapper.map(preferences, UserMailPreferences.class);
            updatedPref.setEmail(email);
        } else {
            updatedPref = optionalCur.get();
            if (preferences.getMailGreetingType() != null) {
                updatedPref.setMailGreetingType(preferences.getMailGreetingType());
            }
        }

        return this.userMailPreferencesRepository.save(updatedPref);
    }
}