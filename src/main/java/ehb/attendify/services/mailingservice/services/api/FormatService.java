package ehb.attendify.services.mailingservice.services.api;

import com.fasterxml.jackson.databind.JsonNode;
import ehb.attendify.services.mailingservice.exceptions.InvalidUserLocation;
import ehb.attendify.services.mailingservice.models.GenericEmail;
import ehb.attendify.services.mailingservice.models.template.Template;
import ehb.attendify.services.mailingservice.models.user.User;

public interface FormatService {

    /**
     * Generates a {@link GenericEmail} with the header parsed out of Object
     * @param template template to generate from
     * @param data templating data
     * @return email
     */
    GenericEmail formatEmail(Template template, JsonNode data) throws InvalidUserLocation;

    /**
     * Returns the template formatted according to the data
     * @param template The template
     * @param data Object to use as data map
     * @return string
     */
    String format(Template template, JsonNode data);

    /**
     * Formats the user's name according to the preferences
     * @param user user parsed from xml
     * @param title include title
     * @return string
     */
    String formatUserName(User user, boolean title);

    /**
     * Formats the user's name according to the preferences without a title
     * @param user user parsed from xml
     * @return string
     */
    default String formatUserName(User user) {
        return formatUserName(user, false);
    }


}
