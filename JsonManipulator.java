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
                        for (String sid : (List<String>) serviceIdObj) {   // Note: Here i did explicit type casting, but we can use another way like above using mapper.converValue()
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
        String sampleJson = "[\n" +                            // Remember this is Java String
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
        System.out.println("Result before making it JSON: "+result);  
        ObjectMapper mapper = new ObjectMapper();
        String outputJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(result);   // Remeber this is JSON String
        System.out.println(outputJson);
    }
}
/*
Result before making it JSON: [{firstName=John, lastName=sean, serviceId=[123, 456], collections={customer={id=1}, product=[{isParent=true, productId=PI123}, {isParent=true, productId=PI456}]}}, {firstName=Jane, lastName=Peter, serviceId=[789], collections={product=[{isParent=true, productId=PI789}]}}]
Output after converting to JSON: [ {
  "firstName" : "John",
  "lastName" : "sean",
  "serviceId" : [ "123", "456" ],
  "collections" : {
    "customer" : {
      "id" : 1
    },
    "product" : [ {
      "isParent" : true,
      "productId" : "PI123"
    }, {
      "isParent" : true,
      "productId" : "PI456"
    } ]
  }
}, {
  "firstName" : "Jane",
  "lastName" : "Peter",
  "serviceId" : [ "789" ],
  "collections" : {
    "product" : [ {
      "isParent" : true,
      "productId" : "PI789"
    } ]
  }
} ]

*/

