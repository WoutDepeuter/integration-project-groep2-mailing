package ehb.attendify.services.mailingservice.controllers;

import ehb.attendify.services.mailingservice.dto.MailUserDto;
import ehb.attendify.services.mailingservice.models.enums.Operation;
import ehb.attendify.services.mailingservice.models.general.AttendifyUserMessage;
import ehb.attendify.services.mailingservice.services.api.UserService;
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
public class MailUserController {

    private final UserService userService;
    private final ModelMapper mapper;

    @RabbitListener(queues = "mailing.user")
    public void onUserUpdate(AttendifyUserMessage msg) {
        var dto = MailUserDto.builder()
                .mailGreetingType(msg.getUser().getTitle())
                .firstName(msg.getUser().getFirstName())
                .lastName(msg.getUser().getLastName())
                .email(msg.getUser().getEmail())
                .build();

        if (msg.getInfo().getOperation().equals(Operation.DELETE)) {
            this.userService.delete(msg.getUser().getId());
            log.info("User {} has been deleted form the database", msg.getUser().getId());
        } else {
            this.userService.updatePreferencesForUser(msg.getUser().getId(), dto);
            log.debug("Updated UserMailPreferences for {}", msg.getUser().getId());
        }
    }

    @GetMapping("/all")
    public Iterable<MailUserDto> getAll() {
        return this.userService.getAllPreferences()
                .stream()
                .map(p -> mapper.map(p, MailUserDto.class))
                .toList();
    }

    @GetMapping("/{userId}")
    public MailUserDto getForUser(@PathVariable String userId) {
        var preferences = this.userService.getPreferencesForUser(userId)
                .orElseThrow(NoSuchElementException::new);
        return this.mapper.map(preferences, MailUserDto.class);
    }

    @PostMapping("/{userId}")
    public ResponseEntity<String> updateForUser(@RequestBody MailUserDto preferences,
                                                @PathVariable String userId) {
        log.info("Received request for user ID: {}", userId);
        log.info("Mail Greeting Type: {}", preferences.getMailGreetingType());

        this.userService.updatePreferencesForUser(userId, preferences);

        return ResponseEntity.ok("User preferences updated successfully!");
    }

}
