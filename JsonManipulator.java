import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;

public class JsonManipulator {

    public static List<Object> processJson(String jsonInput) {
        List<Object> mergedResponse = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();

        try {
            //Here we are parsing JSON into a List of Objects
            List<Object> listingsResponse = mapper.readValue(jsonInput, new TypeReference<List<Object>>() {});

            for (Object item : listingsResponse) {
                  //Here we are converting List of Objects to Map<String, Object>
                Map<String, Object> itemMap = mapper.convertValue(item, new TypeReference<Map<String, Object>>() {});

                //Now collectionsObj will be object as Map has string as key, object as value
                Object collectionsObj = itemMap.get("collections");
                 //type cast the collectionsObj
                Map<String, Object> collectionsMap = (Map<String, Object>) collectionsObj;
                      
                // If product key is missing, for now add a dummy product list
                if (!collectionsMap.containsKey("product")) {
                    List<Object> productResponse = new ArrayList<>();

                    // Extract serviceId and simulate fetching products
                    Object serviceIdObj = itemMap.get("serviceId");
                        for (String sid : (List<String>) serviceIdObj) {
                            if (sid != null) {
                                Map<String, Object> product = new HashMap<>();
                                product.put("productId", "PI" + sid);
                                product.put("isParent", true);
                                productResponse.add(product);
                            }
                        }
                    collectionsMap.put("product", productResponse);
                }

                // Update collections with product collections
                itemMap.put("collections", collectionsMap);
                mergedResponse.add(itemMap);
            }

        } catch (Exception e) {
            throw new RuntimeException("Error processing JSON: " + e.getMessage(), e);
        }

        return mergedResponse;
    }

    public static void main(String[] args) throws Exception {
        String sampleJson = "[\n" +
                "  {\n" +
                "    \"firstName\": \"John\",\n" +
                "    \"lastName\": \"sean\",\n" +
                "    \"serviceId\": [\"123\", \"456\"],\n" +
                "    \"collections\": {\n" +
                "      \"customer\": { \"id\": 1 }\n" +
                "    }\n" +
                "  },\n" +
                "  {\n" +
                "    \"firstName\": \"Jane\",\n" +
                "    \"lastName\": \"Peter\",\n" +
                "    \"serviceId\": [\"789\"],\n" +
                "    \"collections\": {}\n" +
                "  }\n" +
                "]";

        List<Object> result = processJson(sampleJson);
        ObjectMapper mapper = new ObjectMapper();
        String outputJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(result);
        System.out.println(outputJson);
    }
}
/*

Output will be as follows:
[
  {
    "firstName" : "John",
    "lastName" : "sean",
    "serviceId" : [ "123", "456" ],
    "collections" : {
      "customer" : {
        "id" : 1
      },
      "product" : [
        {
          "productId" : "PI123",
          "isParent" : true
        },
        {
          "productId" : "PI456",
          "isParent" : true
        }
      ]
    }
  },
  {
    "firstName" : "Jane",
    "lastName" : "Peter",
    "serviceId" : [ "789" ],
    "collections" : {
      "product" : [
        {
          "productId" : "PI789",
          "isParent" : true
        }
      ]
    }
  }
]



*/

