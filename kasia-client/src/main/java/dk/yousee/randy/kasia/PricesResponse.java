package dk.yousee.randy.kasia;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
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
    private Map<String, ItemPrice> items;
    private Date readTime;
    public PricesResponse(List<String> itemKeys, String message, String kasiaResponse) {
        this.message = message == null ? null : (message.trim().length() == 0 ? null : message);
        this.readTime=new Date();
        if (kasiaResponse != null && kasiaResponse.trim().length() > 0) {
            try {
                jsonSource = new JsonParser().parse(kasiaResponse);
                JsonObject root = jsonSource.getAsJsonObject();

                if (message == null && root.has("fejl")) {
                    this.message = root.get("fejl").getAsString();
                }
                items = parse(itemKeys, jsonSource.getAsJsonObject());


            } catch (JsonSyntaxException e) {
                items = initItems();
                this.message = String.format("Tried to parse kasia response, got syntax error, message: %s", e.getMessage());
            }
        }
    }

    public Collection<ItemPrice> asList() {
        if(items==null)throw new IllegalStateException("Invalid dataset");
        return items.values();
    }

    public Date getReadTime() {
        return readTime;
    }
    static final SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //"2012-09-03 18:00:00");

    public String getReadTimeAsString() {
        return format.format(getReadTime());
    }

    private Map<String, ItemPrice> parse(List<String> itemKeys, JsonObject json) {
        Map<String, ItemPrice> list = initItems();
        for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
            String id = entry.getKey();
            if (!id.trim().isEmpty()) {
                if (itemKeys.contains(id)) {
                    ItemPrice itemPrice = new ItemPrice(id, entry.getValue().getAsJsonObject());
                    list.put(itemPrice.getId(), itemPrice);
                }
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

    public ItemPrice filterByPrice(String price){
        for(ItemPrice item:getItems().values()){
            if(item.matchPrice(price)){
                return item;
            }
        }
        return null;
    }

    public String availablePrices(){
        StringBuilder sb=new StringBuilder();
        for(ItemPrice item:getItems().values()){
            if(sb.length()!=0){
                sb.append(" ");
            }
            sb.append(item.getTotalpris());
        }
        return sb.toString();
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

}
