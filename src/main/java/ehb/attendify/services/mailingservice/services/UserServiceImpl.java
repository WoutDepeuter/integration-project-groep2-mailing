package ehb.attendify.services.mailingservice.services;

import ehb.attendify.services.mailingservice.dto.MailUserDto;
import ehb.attendify.services.mailingservice.models.MailUser;
import ehb.attendify.services.mailingservice.repositories.MailUserRepository;
import ehb.attendify.services.mailingservice.services.api.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final MailUserRepository mailUserRepository;
    private final ModelMapper mapper;

    @Override
    public Optional<String> getEmail(String userId) {
        return this.mailUserRepository.findByUserId(userId).map(MailUser::getEmail);
    }

    @Override
    public void setEmail(String userId, String email) {
        var mailUser = this.mailUserRepository.findByUserId(userId).orElse(new MailUser());
        mailUser.setUserId(userId);
        mailUser.setEmail(email);
        this.mailUserRepository.save(mailUser);
    }

    @Override
    public Optional<MailUser> getPreferencesForUser(String userId) {
        return this.mailUserRepository.findByUserId(userId);
    }

    @Override
    public List<MailUser> getAllPreferences() {
        return this.mailUserRepository.findAll();
    }


    @Override
    public MailUser updatePreferencesForUser(String userId, MailUserDto dto) {
        var mailUser = this.mailUserRepository.findByUserId(userId).orElse(new MailUser());

        mailUser.setUserId(userId);
        if (dto.getFirstName() != null) mailUser.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null) mailUser.setLastName(dto.getLastName());
        if (dto.getMailGreetingType() != null) mailUser.setMailGreetingType(dto.getMailGreetingType());

        return this.mailUserRepository.save(mailUser);
    }
}