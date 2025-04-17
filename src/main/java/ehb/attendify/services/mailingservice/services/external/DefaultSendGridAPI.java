package ehb.attendify.services.mailingservice.services.external;

import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGridAPI;

import java.io.IOException;
import java.util.Map;

public class DefaultSendGridAPI implements SendGridAPI {
    @Override
    public void initialize(String s, String s1) {

    }

    @Override
    public String getLibraryVersion() {
        return "";
    }

    @Override
    public String getVersion() {
        return "";
    }

    @Override
    public void setVersion(String s) {

    }

    @Override
    public Map<String, String> getRequestHeaders() {
        return Map.of();
    }

    @Override
    public Map<String, String> addRequestHeader(String s, String s1) {
        return Map.of();
    }

    @Override
    public Map<String, String> removeRequestHeader(String s) {
        return Map.of();
    }

    @Override
    public String getHost() {
        return "";
    }

    @Override
    public void setHost(String s) {

    }

    @Override
    public Response makeCall(Request request) throws IOException {
        var res = new Response();
        res.setStatusCode(200);
        return res;
    }

    @Override
    public Response api(Request request) throws IOException {
        var res = new Response();
        res.setStatusCode(200);
        return res;
    }
}
