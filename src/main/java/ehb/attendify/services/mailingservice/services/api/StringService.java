package ehb.attendify.services.mailingservice.services.api;

public interface StringService {

    byte[] toByteArray(String string);

    String fromByteArray(byte[] bytes);

}
