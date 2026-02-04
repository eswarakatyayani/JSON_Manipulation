
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonTest {
    public static void main(String[] args) throws Exception {

    	// Java String (Basically it has JSON content String)
        String msg = "{"
                + "\"orderNumber\":\"NC63033228278\","
                + "\"orderVersion\":\"1\","
                + "\"created\":\"01/09/26 15:54:13\","
                + "\"notes\":{"
                + "   \"remarks\":{"
                + "       \"data\":\"Task Completed\""
                + "   }"
                + "},"
                + "\"notesType\":\"remarks\""
                + "}";

        // Step 2: Convert Java String (JSON content String) → JsonNode
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode dataNode = objectMapper.readTree(msg);

        // Print the entire JsonNode
        System.out.println("RAW JsonNode (compact): " + dataNode);

        // Step 3: Extract fields
        String orderNumber = dataNode.path("orderNumber").asText(null);
        String orderVersion = dataNode.path("orderVersion").asText(null);
        String created = dataNode.path("created").asText(null);
        String notesType = dataNode.path("notesType").asText(null);
        JsonNode notesNode = dataNode.path("notes").isMissingNode() ? null : dataNode.path("notes");

        // Step 4: Print extracted values
        System.out.println("\nExtracted Values:");
        System.out.println("orderNumber : " + orderNumber);
        System.out.println("orderVersion: " + orderVersion);
        System.out.println("created     : " + created);
        System.out.println("notesType   : " + notesType);
        System.out.println("notes JSON  : " + notesNode);
    }
}

/*

Output:

RAW JsonNode (compact): {"orderNumber":"NC63033228278","orderVersion":"1","created":"01/09/26 15:54:13","notes":{"remarks":{"data":"Task Completed"}},"notesType":"remarks"}

Extracted Values:
orderNumber : NC63033228278
orderVersion: 1
created     : 01/09/26 15:54:13
notesType   : remarks
notes JSON  : {"remarks":{"data":"Task Completed"}}

*/

