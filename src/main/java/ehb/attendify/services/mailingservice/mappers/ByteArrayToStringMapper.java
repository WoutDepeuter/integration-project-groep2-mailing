package ehb.attendify.services.mailingservice.mappers;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import java.nio.charset.StandardCharsets;

public class ByteArrayToStringMapper implements Converter<byte[], String> {

    @Override
    public String convert(MappingContext<byte[], String> context) {
        byte[] source = context.getSource();
        if (source == null) {
            return null;
        }

        return new String(source, StandardCharsets.UTF_8);
    }
}
