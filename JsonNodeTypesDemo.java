
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonNodeTypesDemo {
    public static void main(String[] args) throws Exception {

        String json = "{"
                + "\"name\":\"Rohan\","
                + "\"age\":25,"
                + "\"salary\":55000.75,"
                + "\"isActive\":true,"
                + "\"address\":{\"city\":\"Hyderabad\",\"zip\":500001},"
                + "\"skills\":[\"Java\",\"Kafka\",\"SQL\"],"
                + "\"middleName\":null"
                + "}";

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(json);

        printNodeInfo(root, "name");
        printNodeInfo(root, "age");
        printNodeInfo(root, "salary");
        printNodeInfo(root, "isActive");
        printNodeInfo(root, "address");
        printNodeInfo(root, "skills");
        printNodeInfo(root, "middleName");
        printNodeInfo(root, "unknownField"); // MissingNode example
    }

    private static void printNodeInfo(JsonNode root, String fieldName) {
        JsonNode node = root.path(fieldName);

        System.out.println("\nField: " + fieldName);
        System.out.println("Node Type: " + node.getNodeType());
        System.out.println("Java Class: " + node.getClass().getSimpleName());

        if (node.isTextual()) {
            System.out.println("Text Value: " + node.asText());
        } else if (node.isInt()) {
            System.out.println("Int Value: " + node.asInt());
        } else if (node.isDouble()) {
            System.out.println("Double Value: " + node.asDouble());
        } else if (node.isBoolean()) {
            System.out.println("Boolean Value: " + node.asBoolean());
        } else if (node.isObject()) {
            System.out.println("Object Fields: " + node.fieldNames().next());
        } else if (node.isArray()) {
            System.out.println("Array Size: " + node.size());
        } else if (node.isNull()) {
            System.out.println("Value is explicitly null");
        } else if (node.isMissingNode()) {
            System.out.println("Field is missing in JSON");
        }
    }
}

/* 
 Output:
 
Field: name
Node Type: STRING
Java Class: TextNode
Text Value: Rohan

Field: age
Node Type: NUMBER
Java Class: IntNode
Int Value: 25

Field: salary
Node Type: NUMBER
Java Class: DoubleNode
Double Value: 55000.75

Field: isActive
Node Type: BOOLEAN
Java Class: BooleanNode
Boolean Value: true

Field: address
Node Type: OBJECT
Java Class: ObjectNode
Object Fields: city

Field: skills
Node Type: ARRAY
Java Class: ArrayNode
Array Size: 3

Field: middleName
Node Type: NULL
Java Class: NullNode
Value is explicitly null

Field: unknownField
Node Type: MISSING
Java Class: MissingNode
Field is missing in JSON
 
 
 */

// JsonNode is best when JSON is dynamic.(Like Above) POJOs are best when JSON is stable.

// writeValueAsString(): Java Object → JSON String

// readTree():	JSON String → Json Node

// readValue()	JSON String → Java Object


/*
   SOME USE CASES BELOW
   
 1️. Reading API Request Bodies (Dynamic JSON)

When clients send flexible JSON and you don’t want to create a POJO for every variation.

@PostMapping("/process")
public ResponseEntity<?> process(@RequestBody JsonNode body) {
    String type = body.path("type").asText();
    JsonNode payload = body.path("payload");
    // handle based on type
}


✔ Useful when JSON schema changes often
✔ Avoids creating too many model classes

 2. Working with Kafka Messages (Your Case)

When message format may change or contain optional fields.

JsonNode root = objectMapper.readTree(msg);

if (root.has("orderNumber")) {
    // new format
} else if (root.has("value")) {
    // old wrapped format
}


✔ Backward compatibility
✔ Schema evolution handling

 3. Extracting Only Few Fields from Huge JSON

Instead of mapping the whole thing to a POJO.

JsonNode root = mapper.readTree(hugeJson);
String id = root.path("meta").path("requestId").asText();


✔ Faster
✔ Less memory
✔ Cleaner when you need only 2–3 fields

 4. JSON Validation

Check if required fields exist.

if (!root.hasNonNull("orderNumber")) {
    throw new IllegalArgumentException("orderNumber is required");
}


✔ Prevents null pointer issues
✔ Useful before DB insert

 5️. Transforming JSON (Modify Before Sending)

You can edit JSON dynamically.

ObjectNode root = (ObjectNode) mapper.readTree(json);
root.put("processedAt", Instant.now().toString());
root.remove("internalField");


✔ Add/remove fields
✔ Mask sensitive data
✔ Prepare payloads for other systems

 6️. Calling External APIs

Sometimes API responses are unpredictable.

JsonNode response = mapper.readTree(apiResponse);

if (response.path("status").asText().equals("SUCCESS")) {
    JsonNode data = response.path("result");
}

✔ Avoids breaking when API adds extra fields

 7️. Logging & Debugging Unknown JSON
log.info("Incoming JSON:\n{}", root.toPrettyString());


✔ Understand live production payloads

 8️. Handling Arrays Without POJOs
for (JsonNode item : root.path("items")) {
    System.out.println(item.path("name").asText());
}


✔ Useful for bulk data
✔ No need to create List<MyClass>

 9️. Converting Between JSON and POJO Partially
JsonNode node = mapper.readTree(json);
MyPojo pojo = mapper.treeToValue(node.path("user"), MyPojo.class);


✔ Parse only part of JSON into object
*/
 
 
 
 
 
