package ehb.attendify.services.mailingservice.models.enums;

import lombok.Getter;

@Getter
public enum ContentType {
    TEXT_PLAIN("text/plain"),
    TEXT_HTML("text/html");

    private final String type;

    ContentType(String type) {
        this.type = type;
    }

}
