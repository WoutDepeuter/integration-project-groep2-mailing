package ehb.attendify.services.mailingservice.services.mailingservice.singleton;

import com.sendgrid.SendGrid;

public class SendGridSingleton {

    private SendGridSingleton() {}

    private static class SingletonHolder {
        private static final SendGrid INSTANCE = new SendGrid(System.getenv("SENDGRID_API_KEY"));
    }

    public static SendGrid getInstance() {
        return SingletonHolder.INSTANCE;
    }
}
