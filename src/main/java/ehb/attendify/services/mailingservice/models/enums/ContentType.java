package ehb.attendify.services.mailingservice.models.enums;

public enum ContentType {
    TEXT_PLAIN("text/plain"),
    TEXT_HTML("text/html");

    private final String type;

    ContentType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
