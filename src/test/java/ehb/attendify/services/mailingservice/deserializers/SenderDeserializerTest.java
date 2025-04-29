package ehb.attendify.services.mailingservice.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import ehb.attendify.services.mailingservice.models.enums.Sender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SenderDeserializerTest {
    private SenderDeserializer deserializer;
    private JsonParser parser;
    private DeserializationContext context;

    @BeforeEach
    void setUp() {
        deserializer = new SenderDeserializer();
        parser = mock(JsonParser.class);
        context = mock(DeserializationContext.class);
    }

    @Test
    void testDeserializeFromString() throws Exception {
        when(parser.getText()).thenReturn("crm");

        Sender result = deserializer.deserialize(parser, context);
        assertEquals(Sender.CRM, result);
    }

    @Test
    void testDeserializeFromStringCaseInsensitive() throws Exception {
        when(parser.getText()).thenReturn("PoS");

        Sender result = deserializer.deserialize(parser, context);
        assertEquals(Sender.POS, result);
    }

    @Test
    void testDeserializeFromOrdinal() throws Exception {
        when(parser.getText()).thenReturn("1");
        when(parser.getIntValue()).thenReturn(1);

        Sender result = deserializer.deserialize(parser, context);
        assertEquals(Sender.POS, result);
    }

    @Test
    void testDeserializeUnknownValueThrowsException() throws Exception {
        when(parser.getText()).thenReturn("invalid_sender");
        when(parser.getIntValue()).thenThrow(new NumberFormatException());

        assertThrows(IllegalArgumentException.class, () -> {
            deserializer.deserialize(parser, context);
        });
    }


    @Test
    void testDeserializeInvalidOrdinalReturnsNull() throws Exception {
        when(parser.getText()).thenReturn("99");
        when(parser.getIntValue()).thenReturn(99);

        Sender result = deserializer.deserialize(parser, context);
        assertNull(result);
    }

    @Test
    void testDeserializeNullReturnsNull() throws Exception {
        when(parser.getText()).thenReturn(null);

        Sender result = deserializer.deserialize(parser, context);
        assertNull(result);
    }
}
