package ehb.attendify.services.mailingservice.services;

import ehb.attendify.services.mailingservice.services.api.StringService;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class StringServiceImpl implements StringService {

    @Override
    public byte[] toByteArray(String source) {
        return source.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public String fromByteArray(byte[] source) {
        return new String(source, StandardCharsets.UTF_8);
    }
}
