package ehb.attendify.services.mailingservice.controllers;

import ehb.attendify.services.mailingservice.dto.UserMailPreferencesDto;
import ehb.attendify.services.mailingservice.services.api.UserMailPreferencesService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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
public class UserMailPreferencesController {

    private final UserMailPreferencesService mailPreferencesService;
    private final ModelMapper mapper;

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
        System.out.println("Received request for user ID: " + userId);
        System.out.println("Mail Greeting Type: " + preferences.getMailGreetingType());

        this.mailPreferencesService.updatePreferencesForUser(userId, preferences);

        return ResponseEntity.ok("User preferences updated successfully!");
    }

}
