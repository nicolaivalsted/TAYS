package dk.yousee.randy.yspro;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author m27236
 */
public class ProStoreResponse {
    private JsonElement jsonSource;
    private boolean exists;
    private Integer status;
    private String message;
    private YsProTime dateTime;
    private List<StoreProduct> products;

    public ProStoreResponse(String jsonYsProResponse) throws JsonSyntaxException {
        jsonSource = new JsonParser().parse(jsonYsProResponse);
        JsonObject root = jsonSource.getAsJsonObject();
        build(root);
    }
    
    public ProStoreResponse(JsonObject json){
        build(json);
    }
    
    public ProStoreResponse() {
        status=0;
        message="No update";
        products=new ArrayList<StoreProduct>();
    }
    
    private void build(JsonObject root) {
        status = root.get("Status").getAsInt();
        message = root.get("Message").getAsString();
        JsonElement element = root.get("DateTime");
        if (element != null) {
            dateTime = YsProTime.create(root.get("DateTime").getAsString());
        }
        JsonElement dataElement;        
        if (root.has("Products")) {
            dataElement = root.get("Products");
        } else {
            dataElement = root.get("Data");
        }

        if (dataElement!=null && !dataElement.isJsonPrimitive()) {
            exists = true;
            products = parseData(dataElement.getAsJsonArray());
        } else {
            products = new ArrayList<StoreProduct>();
        }
    }

    public boolean isExists() {
        return exists;
    }

    public Integer getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public YsProTime getDateTime() {
        return dateTime;
    }

    public List<StoreProduct> getProducts() {
        return products;
    }

    public List<StoreProduct> filterOttProduct(String key) {
        List<StoreProduct> res = new ArrayList<StoreProduct>();
        for (StoreProduct p : products) {
            if (key.equals(p.getProperties().get(ProStoreDef.OTT_PRODUCT_KEY_YSPRO))) {
                res.add(p);
            }
        }
        return res;
    }

    public List<StoreProduct> filterOttProductWithSignal(String key) {
        List<StoreProduct> res = new ArrayList<StoreProduct>();
        for (StoreProduct p : products) {
            if (key.equals(p.getProperties().get(ProStoreDef.OTT_PRODUCT_KEY_YSPRO))) {
                if (p.hasSignal(dateTime)) {
                    res.add(p);
                }
            }
        }
        return res;
    }

    public List<StoreProduct> filterServiceItem(String key, boolean onlyOpen) {
        List<StoreProduct> res = new ArrayList<StoreProduct>();
        List<StoreProduct> source = products;
        if (onlyOpen)
            source = filterOpen();
        for (StoreProduct p : source) {
            if (key.equals(p.getProperties().get(ProStoreDef.SERVICE_ITEM_KEY_YSPRO))) {
                res.add(p);
            }
        }
        return res;
    }

    public List<StoreProduct> filterOpen() {
        List<StoreProduct> res = new ArrayList<StoreProduct>();
        for (StoreProduct p : products) {
            if (p.hasSignal(dateTime)) {
                res.add(p);
            }
        }
        return res;
    }
    
    /**
     * 
     * @param customer 
     * @return List with customer numbers not the same as param 
     */
    public List<StoreProduct> filterCustomerDifferntFrom(String customer){
        List<StoreProduct> res = new ArrayList<StoreProduct>();
        for(StoreProduct p : products){
            if(p.getCustomer()!=null && !p.getCustomer().equals(customer)) {
                res.add(p);
            }
        }
        return res;
    }

    private List<StoreProduct> parseData(JsonArray in) {
        List<StoreProduct> res = new ArrayList<StoreProduct>();
        for (JsonElement one : in) {
            JsonObject json = one.getAsJsonObject();
            res.add(new StoreProduct(res.size(), json));
        }
        return res;
    }
    
    public JsonElement printJson() {
        JsonObject res = new JsonObject();
        res.addProperty("Status", status);
        res.addProperty("Message", message);
        if (dateTime != null) res.addProperty("DateTime", dateTime.toString());
        if (!exists) res.addProperty("exists", false);
        JsonArray array = new JsonArray();
        for (StoreProduct product : getProducts()) {
            array.add(product.printJson());
        }
        res.add("items", array);
        return res;
    }

    @Override
    public String toString() {
        return new Gson().toJson(jsonSource);
    }
}
