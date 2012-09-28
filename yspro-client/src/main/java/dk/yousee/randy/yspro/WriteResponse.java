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
 * Date: 11/09/12
 * Time: 10.38
 * Response from writing to ProStore
 */
public class WriteResponse  {

    private Integer status;
    private String message;
    private String dateTime;
    private List<StoreProduct> products;

    public WriteResponse() {
        status=0;
        message="No update";
        products=new ArrayList<StoreProduct>();
    }

    public WriteResponse(String input) throws JsonParseException {
        JsonElement em= new JsonParser().parse(input);
        JsonObject jo=em.getAsJsonObject();
        parse(jo);
    }
    public WriteResponse(JsonObject jo)  {
        parse(jo);
    }

    public List<StoreProduct> getProducts() {
        return products;
    }

    public Integer getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

//    public ProcessError getError() {
//        if(status!=0) {
//            return new ProcessError(SyncError.PROSTORE_FAILED,String.format(
//                "Tried to update prostore got status %s with message %s",status,message));
//        }
//        return null;
//    }

    public void parse(JsonObject jo) {

        status=jo.get("Status").getAsInt();
        message=jo.get("Message").getAsString();
        JsonElement dt = jo.get("DateTime");
        if(dt!=null)dateTime=dt.getAsString();
        JsonElement in=jo.get("Data");
        if(in==null) {
            products=new ArrayList<StoreProduct>();
        } else{
            products=parseData(in.getAsJsonArray());
        }
    }

    private List<StoreProduct> parseData(JsonArray in) {
        List<StoreProduct> res=new ArrayList<StoreProduct>();
        for (JsonElement one : in) {
            JsonObject json = one.getAsJsonObject();
            res.add(new StoreProduct(json));
        }
        return res;
    }

    public JsonElement printJson() {
        JsonObject res=new JsonObject();
        res.addProperty("Status",status);
        res.addProperty("Message",message);
        res.addProperty("DateTime",dateTime);
        JsonArray array = new JsonArray();
        for (StoreProduct product : getProducts()) {
            array.add(product.printJson());
        }
        res.add("Data",array);
        return res;
    }
}
