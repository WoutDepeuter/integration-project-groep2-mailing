package ehb.attendify.services.mailingservice.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import ehb.attendify.services.mailingservice.exceptions.InvalidUserLocation;
import ehb.attendify.services.mailingservice.models.mail.header.Header;
import ehb.attendify.services.mailingservice.models.mail.header.Recipient;
import ehb.attendify.services.mailingservice.models.template.Template;
import ehb.attendify.services.mailingservice.models.template.UserLocationType;
import ehb.attendify.services.mailingservice.services.api.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MessageMapperServiceTest {

    @Mock
    private XmlMapper xmlMapper;

    @Mock
    private UserService userService;

    @InjectMocks
    private MessageMapperServiceImpl messageMapperService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void map_shouldConvertMessageToJsonNode() throws Exception {
        byte[] messageBody = "<root><data>test</data></root>".getBytes();
        Message message = new Message(messageBody, new MessageProperties());

        JsonNode expectedNode = objectMapper.createObjectNode();
        ((ObjectNode) expectedNode).put("data", "test");

        when(xmlMapper.readTree(messageBody)).thenReturn(expectedNode);

        JsonNode result = messageMapperService.map(message);

        assertEquals(expectedNode, result);
    }

    @Test
    void extractHeader_withSingleUserLocationType_shouldReturnHeaderWithSingleRecipient() throws Exception {
        Template template = Template.builder()
                .userLocation("user.id")
                .userLocationType(UserLocationType.SINGLE)
                .subject("Test Subject")
                .build();

        ObjectNode jsonNode = objectMapper.createObjectNode();
        ObjectNode userNode = objectMapper.createObjectNode();
        userNode.put("id", "user123");
        jsonNode.set("user", userNode);

        when(userService.getEmail("user123")).thenReturn(Optional.of("user123@example.com"));

        Header result = messageMapperService.extractHeader(template, jsonNode);

        assertEquals("Test Subject", result.getSubject());
        assertEquals(1, result.getRecipients().size());
        assertEquals("user123@example.com", result.getRecipients().get(0).getEmail());
    }

    @Test
    void extractHeader_withArrayUserLocationType_shouldReturnHeaderWithMultipleRecipients() throws Exception {
        Template template = Template.builder()
                .userLocation("users")
                .userLocationType(UserLocationType.ARRAY)
                .subject("Test Subject")
                .build();

        ObjectNode jsonNode = objectMapper.createObjectNode();
        ArrayNode usersArray = objectMapper.createArrayNode();
        usersArray.add("user1");
        usersArray.add("user2");
        jsonNode.set("users", usersArray);

        when(userService.getEmail("user1")).thenReturn(Optional.of("user1@example.com"));
        when(userService.getEmail("user2")).thenReturn(Optional.of("user2@example.com"));

        Header result = messageMapperService.extractHeader(template, jsonNode);

        // Assert
        assertEquals("Test Subject", result.getSubject());
        assertEquals(2, result.getRecipients().size());
        assertEquals("user1@example.com", result.getRecipients().get(0).getEmail());
        assertEquals("user2@example.com", result.getRecipients().get(1).getEmail());
    }

    @Test
    void extractHeader_withEmptyUserLocation_shouldThrowInvalidUserLocation() {
        Template template = Template.builder()
                .userLocation("")
                .userLocationType(UserLocationType.SINGLE)
                .subject("Test Subject")
                .build();

        ObjectNode jsonNode = objectMapper.createObjectNode();

        assertThrows(InvalidUserLocation.class, () ->
            messageMapperService.extractHeader(template, jsonNode)
        );
    }

    @Test
    void extractHeader_withInvalidUserPath_shouldThrowInvalidUserLocation() {
        Template template = Template.builder()
                .userLocation("user.id")
                .userLocationType(UserLocationType.SINGLE)
                .subject("Test Subject")
                .build();

        ObjectNode jsonNode = objectMapper.createObjectNode();

        assertThrows(InvalidUserLocation.class, () ->
            messageMapperService.extractHeader(template, jsonNode)
        );
    }

    @Test
    void extractHeader_withNonArrayForArrayType_shouldThrowInvalidUserLocation() {
        Template template = Template.builder()
                .userLocation("user")
                .userLocationType(UserLocationType.ARRAY)
                .subject("Test Subject")
                .build();

        ObjectNode jsonNode = objectMapper.createObjectNode();
        jsonNode.put("user", "not-an-array");

        assertThrows(InvalidUserLocation.class, () ->
            messageMapperService.extractHeader(template, jsonNode)
        );
    }

    @Test
    void extractHeader_withNonTextualNodeForSingle_shouldThrowInvalidUserLocation() {
        Template template = Template.builder()
                .userLocation("user")
                .userLocationType(UserLocationType.SINGLE)
                .subject("Test Subject")
                .build();

        ObjectNode jsonNode = objectMapper.createObjectNode();
        ObjectNode userNode = objectMapper.createObjectNode();
        jsonNode.set("user", userNode);

        assertThrows(InvalidUserLocation.class, () ->
            messageMapperService.extractHeader(template, jsonNode)
        );
    }

    @Test
    void extractHeader_whenUserServiceReturnsEmptyOptional_shouldUseUserIdAsEmail() throws Exception {
        Template template = Template.builder()
                .userLocation("user.id")
                .userLocationType(UserLocationType.SINGLE)
                .subject("Test Subject")
                .build();

        ObjectNode jsonNode = objectMapper.createObjectNode();
        ObjectNode userNode = objectMapper.createObjectNode();
        userNode.put("id", "user123");
        jsonNode.set("user", userNode);

        when(userService.getEmail("user123")).thenReturn(Optional.empty());

        Header result = messageMapperService.extractHeader(template, jsonNode);

        assertEquals("Test Subject", result.getSubject());
        assertEquals(1, result.getRecipients().size());
        assertEquals("user123", result.getRecipients().getFirst().getEmail());
    }
}
