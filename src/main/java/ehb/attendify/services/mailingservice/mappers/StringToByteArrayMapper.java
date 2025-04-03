package ehb.attendify.services.mailingservice.mappers;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import java.nio.charset.StandardCharsets;

public class StringToByteArrayMapper implements Converter<String, byte[]> {

    @Override
    public byte[] convert(MappingContext<String, byte[]> context) {
        String source = context.getSource();
        if (source == null) {
            return null;
        }
        return source.getBytes(StandardCharsets.UTF_8);
    }
}
