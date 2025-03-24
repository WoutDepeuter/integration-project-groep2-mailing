package ehb.attendify.services.mailingservice.services;

import ehb.attendify.services.mailingservice.dto.UserMailPreferencesDto;
import ehb.attendify.services.mailingservice.models.UserMailPreferences;
import ehb.attendify.services.mailingservice.repositories.UserMailPreferencesRepository;
import ehb.attendify.services.mailingservice.services.api.UserMailPreferencesService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserMailPreferencesServiceImpl implements UserMailPreferencesService {

    private final UserMailPreferencesRepository userMailPreferencesRepository;
    private final ModelMapper mapper;

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
        if (optionalCur.isEmpty()) {
            var pref = this.mapper.map(preferences, UserMailPreferences.class);
            pref.setUserId(userId);
            return this.userMailPreferencesRepository.saveAndFlush(pref);
        }

        var cur = optionalCur.get();

        if (Objects.nonNull(preferences.getMailGreetingType())) {
            cur.setMailGreetingType(preferences.getMailGreetingType());
        }


        return this.userMailPreferencesRepository.save(cur);
    }
}
