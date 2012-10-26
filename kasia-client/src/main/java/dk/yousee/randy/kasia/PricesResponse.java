package dk.yousee.randy.kasia;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.util.Map;
import java.util.TreeMap;

/**
 * User: aka
 * Date: 19/10/12
 * Time: 10.00
 * Response from reading prices
 */
public class PricesResponse {

    private JsonElement jsonSource;
    private String message;
    private Map<String,ItemPrice> items;

    public PricesResponse(String message, String kasiaResponse) {
        this.message = message == null ? null : (message.trim().length() == 0 ? null : message);
        if (kasiaResponse != null && kasiaResponse.trim().length() > 0) {
            try {
                jsonSource = new JsonParser().parse(kasiaResponse);
                JsonObject root = jsonSource.getAsJsonObject();

                if(message==null && root.has("fejl")){
                    this.message=root.get("fejl").getAsString();
                }
                items=parse(jsonSource.getAsJsonObject());


            } catch (JsonSyntaxException e) {
                items=initItems();
                this.message = String.format("Tried to parse kasia response, got syntax error, message: %s", e.getMessage());
            }
        }
    }


    private Map<String, ItemPrice> parse(JsonObject json) {
        Map<String,ItemPrice> list= initItems();
        for(Map.Entry<String,JsonElement> entry:json.entrySet()){
            String id=entry.getKey();
            if(!id.trim().isEmpty()){
                ItemPrice itemPrice = new ItemPrice(id, entry.getValue().getAsJsonObject());
                list.put(itemPrice.getId(),itemPrice);
            }
        }
        return list;
    }

    private TreeMap<String, ItemPrice> initItems() {
        return new TreeMap<String, ItemPrice>();
    }

    public JsonElement getJsonSource() {
        return jsonSource;
    }

    /**
     * @return not null string means problems !!!!
     */
    public String getMessage() {
        return message;
    }

    public Map<String, ItemPrice> getItems() {
        return items;
    }

    public class ItemPrice {
        private String id;
        private JsonObject json;

        public ItemPrice(String id, JsonObject json) {
            this.id=id;
            this.json=json;
        }

        public String getId() {
            return id;
        }

        public String getTotalpris() {
            return json==null?null:json.get("totalpris").getAsString();
        }

        public int getFejlkode() {
            return json==null?0:json.get("fejlkode").getAsInt();
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("{\"id\":\"").append(id).append('"');
            if(getTotalpris()!=null)sb.append(", \"totalPris\":").append('"').append(getTotalpris()).append('"');
            if(getFejlkode()!=0)sb.append(", \"error\":").append(getFejlkode());
            sb.append('}');
            return sb.toString();
        }
    }
}
