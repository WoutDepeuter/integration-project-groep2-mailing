package ehb.attendify.services.mailingservice.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class FormatServiceTest {

    private final ObjectMapper mapper = new ObjectMapper();
    private final FormatServiceImpl formatService = new FormatServiceImpl(null, null, null);

    @Test
    void format() {
    }

    @Test
    public void testExtractNode_withObjectNode() throws Exception {
        String json = "{\"name\":\"John\", \"age\":\"30\"}";
        JsonNode node = mapper.readTree(json);

        Object result = formatService.extractNode(node);
        assertInstanceOf(Map.class, result);

        Map<?, ?> map = (Map<?, ?>) result;
        assertEquals(2, map.size());
        assertEquals("John", map.get("name"));
        assertEquals("30", map.get("age"));
    }

    @Test
    public void testExtractNode_withNestedObjectNode() throws Exception {
        String json = "{\"user\":{\"name\":\"John\", \"age\":\"30\"}}";
        JsonNode node = mapper.readTree(json);

        Object result = formatService.extractNode(node);
        assertInstanceOf(Map.class, result);

        Map<?, ?> map = (Map<?, ?>) result;
        assertInstanceOf(Map.class, map.get("user"));

        Map<?, ?> userMap = (Map<?, ?>) map.get("user");
        assertEquals("John", userMap.get("name"));
        assertEquals("30", userMap.get("age"));
    }

    @Test
    public void testExtractNode_withArrayNode() throws Exception {
        String json = "[\"apple\", \"banana\", \"cherry\"]";
        JsonNode node = mapper.readTree(json);

        Object result = formatService.extractNode(node);
        assertInstanceOf(List.class, result);

        List<?> list = (List<?>) result;
        assertEquals(3, list.size());
        assertEquals("apple", list.get(0));
        assertEquals("banana", list.get(1));
        assertEquals("cherry", list.get(2));
    }

    @Test
    public void testExtractNodeObjectWithArray() throws Exception {
        String json = "{\"fruits\": [\"apple\", \"banana\", \"cherry\"], \"count\": \"3\"}";
        JsonNode node = mapper.readTree(json);

        Object result = formatService.extractNode(node);
        assertInstanceOf(Map.class, result);

        Map<?, ?> map = (Map<?, ?>) result;

        assertEquals("3", map.get("count"));

        assertInstanceOf(List.class, map.get("fruits"));
        List<?> fruitsList = (List<?>) map.get("fruits");
        assertEquals(3, fruitsList.size());
        assertEquals("apple", fruitsList.get(0));
        assertEquals("banana", fruitsList.get(1));
        assertEquals("cherry", fruitsList.get(2));
    }

}