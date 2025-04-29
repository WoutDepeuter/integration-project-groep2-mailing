package ehb.attendify.services.mailingservice;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import ehb.attendify.services.mailingservice.deserializers.OperationDeserializer;
import ehb.attendify.services.mailingservice.models.enums.Operation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OperationDeserializerTest {

    private OperationDeserializer deserializer;
    private JsonParser parser;
    private DeserializationContext context;

    @BeforeEach
    void setUp() {
        deserializer = new OperationDeserializer();
        parser = mock(JsonParser.class);
        context = mock(DeserializationContext.class);
    }

    @Test
    void testDeserializeFromString() throws Exception {
        when(parser.getText()).thenReturn("create");

        Operation result = deserializer.deserialize(parser, context);
        assertEquals(Operation.CREATE, result);
    }

    @Test
    void testDeserializeFromStringCaseInsensitive() throws Exception {
        when(parser.getText()).thenReturn("UpDaTe");

        Operation result = deserializer.deserialize(parser, context);
        assertEquals(Operation.UPDATE, result);
    }

    @Test
    void testDeserializeFromOrdinal() throws Exception {
        when(parser.getText()).thenReturn("2");
        when(parser.getIntValue()).thenReturn(2);

        Operation result = deserializer.deserialize(parser, context);
        assertEquals(Operation.DELETE, result);
    }

    @Test
    void testDeserializeUnknownValueThrows() throws Exception {
        when(parser.getText()).thenReturn("UNKNOWN");
        when(parser.getIntValue()).thenThrow(new NumberFormatException());

        assertThrows(IllegalArgumentException.class, () -> {
            deserializer.deserialize(parser, context);
        });
    }


    @Test
    void testDeserializeInvalidIntOrdinal() throws Exception {
        when(parser.getText()).thenReturn("100");
        when(parser.getIntValue()).thenReturn(100);

        Operation result = deserializer.deserialize(parser, context);
        assertNull(result);
    }

    @Test
    void testDeserializeNull() throws Exception {
        when(parser.getText()).thenReturn(null);

        Operation result = deserializer.deserialize(parser, context);
        assertNull(result);
    }

}
