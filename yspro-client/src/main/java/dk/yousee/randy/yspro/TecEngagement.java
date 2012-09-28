package dk.yousee.randy.yspro;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: aka
 * Date: 07/09/12
 * Time: 14.48
 * Technical engagement
 */
public class TecEngagement  {


    public static TecEngagement create(String input) throws JsonParseException  {
        TecEngagement te = new TecEngagement();

        JsonElement em= new JsonParser().parse(input);
        JsonObject jo=em.getAsJsonObject();
        te.parseTopLevel(jo);
        JsonElement dataElement = jo.get("Products");
        if (dataElement != null) {
            te.exists = true;
            te.products = te.parseData(dataElement.getAsJsonArray());
        } else {
            te.products = new ArrayList<StoreProduct>();
        }
        return te;
    }

    private boolean exists;
    private Integer status;
    private String message;
    private YsProTime dateTime;
    private List<StoreProduct> products;

    private TecEngagement()  {
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

    public List<StoreProduct> getProducts() {
        return products;
    }

//    public ProcessError getError() {
//        if(status!=0){
//            return new ProcessError(SyncError.PROSTORE_CONTENT,String.format("Reading from ProStore status:%s, %s",status,message));
//        } else {
//            return null;
//        }
//    }


    public void parseTopLevel(JsonObject jo)  {
        status = jo.get("Status").getAsInt();
        message = jo.get("Message").getAsString();
        JsonElement element = jo.get("DateTime");
        if (element != null) dateTime = YsProTime.create(jo.get("DateTime").getAsString());
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

    public List<StoreProduct> filterServiceItem(String key,boolean onlyOpen) {
        List<StoreProduct> res = new ArrayList<StoreProduct>();
        List<StoreProduct> source=products;
        if(onlyOpen)source=filterOpen();
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

    private List<StoreProduct> parseData(JsonArray in) {
        List<StoreProduct> res = new ArrayList<StoreProduct>();
        for (JsonElement one : in) {
            JsonObject json = one.getAsJsonObject();
            res.add(new StoreProduct(res.size(),json));
        }
        return res;
    }
}
