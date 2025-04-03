package ehb.attendify.services.mailingservice.controllers;

import ehb.attendify.services.mailingservice.dto.UserMailPreferencesDto;
import ehb.attendify.services.mailingservice.models.general.AttendifyMessage;
import ehb.attendify.services.mailingservice.models.user.User;
import ehb.attendify.services.mailingservice.services.api.UserMailPreferencesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/UserMailPreferences")
@RequiredArgsConstructor
@Log4j2
public class UserMailPreferencesController {

    private final UserMailPreferencesService mailPreferencesService;
    private final ModelMapper mapper;

    @RabbitListener(queues = "mailing.user")
    public void onUserUpdate(AttendifyMessage<User> userAttendifyMessage) {
        var dto = UserMailPreferencesDto.builder()
                .mailGreetingType(userAttendifyMessage.getUser().getTitle())
                .build();
        var email = userAttendifyMessage.getUser().getEmail();

        this.mailPreferencesService.updatePreferencesForUserByEmail(email, dto);
        log.debug("Updated UserMailPreferences for {}", email);
    }

    @GetMapping("/all")
    public Iterable<UserMailPreferencesDto> getAll() {
        return this.mailPreferencesService.getAllPreferences()
                .stream()
                .map(p -> mapper.map(p, UserMailPreferencesDto.class))
                .toList();
    }

    @GetMapping("/{userId}")
    public UserMailPreferencesDto getForUser(@PathVariable Long userId) {
        var preferences = this.mailPreferencesService.getPreferencesForUser(userId)
                .orElseThrow(NoSuchElementException::new);
        return this.mapper.map(preferences, UserMailPreferencesDto.class);
    }

    @PostMapping("/{userId}")
    public ResponseEntity<String> updateForUser(@RequestBody UserMailPreferencesDto preferences,
                                                @PathVariable Long userId) {

        log.info("Received request for user ID: {}", userId);
        log.info("Mail Greeting Type: {}", preferences.getMailGreetingType());

        this.mailPreferencesService.updatePreferencesForUser(userId, preferences);

        return ResponseEntity.ok("User preferences updated successfully!");
    }

}
