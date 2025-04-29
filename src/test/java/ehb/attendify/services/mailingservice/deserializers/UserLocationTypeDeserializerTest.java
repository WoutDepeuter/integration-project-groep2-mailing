package ehb.attendify.services.mailingservice.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import ehb.attendify.services.mailingservice.models.template.UserLocationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserLocationTypeDeserializerTest {
    private UserLocationTypeDeserializer deserializer;
    private JsonParser parser;
    private DeserializationContext context;

    @BeforeEach
    void setUp() {
        deserializer = new UserLocationTypeDeserializer();
        parser = mock(JsonParser.class);
        context = mock(DeserializationContext.class);
    }

    @Test
    void testDeserializeFromString() throws Exception {
        when(parser.getText()).thenReturn("SINGLE");

        UserLocationType result = deserializer.deserialize(parser, context);
        assertEquals(UserLocationType.SINGLE, result);
    }

    @Test
    void testDeserializeFromStringCaseInsensitive() throws Exception {
        when(parser.getText()).thenReturn("array");

        UserLocationType result = deserializer.deserialize(parser, context);
        assertEquals(UserLocationType.ARRAY, result);
    }

    @Test
    void testDeserializeFromOrdinal() throws Exception {
        when(parser.getText()).thenReturn("1");
        when(parser.getIntValue()).thenReturn(1);

        UserLocationType result = deserializer.deserialize(parser, context);
        assertEquals(UserLocationType.ARRAY, result);
    }

    @Test
    void testDeserializeUnknownValueThrowsException() throws Exception {
        when(parser.getText()).thenReturn("invalid_location_type");
        when(parser.getIntValue()).thenThrow(new NumberFormatException());

        assertThrows(IllegalArgumentException.class, () -> {
            deserializer.deserialize(parser, context);
        });
    }

    @Test
    void testDeserializeInvalidOrdinalThrowsException() throws Exception {
        when(parser.getText()).thenReturn("99");
        when(parser.getIntValue()).thenReturn(99);

        assertThrows(IllegalArgumentException.class, () -> {
            deserializer.deserialize(parser, context);
        });
    }


    @Test
    void testDeserializeNullReturnsNull() throws Exception {
        when(parser.getText()).thenReturn(null);

        UserLocationType result = deserializer.deserialize(parser, context);
        assertNull(result);
    }
}
