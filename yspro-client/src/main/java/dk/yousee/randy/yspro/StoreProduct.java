package dk.yousee.randy.yspro;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.Date;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
* User: aka
* Date: 07/09/12
* Time: 16.07
* One row of data in Pro Store.
*/
public class StoreProduct {

    public static final String POS_KEY = "pos";
    public static final String UUID_KEY = "UUID";
    public static final String FROM_DATE_KEY = "From";
    public static final String TO_DATE_KEY = "To";
    public static final String STATUS_KEY = "Status";
    public static final String ORDER_4_SAME = "OrderForSame";
    public static final String CUSTOMER_NO = "CustomerNumber";
    public static final String PRODUCT_ID_YSPRO = "ProductID";

    private Integer pos;
    private YsProProduct product;
    private YsProTime from;
    private YsProTime to;
    private String uuid;
    private String status;
    private String orderForSame;
    private String customer;
    private Map<String,String> properties=new HashMap<String, String>();
    StringBuilder errorMessage=new StringBuilder();

    public Integer getPos() {
        return pos;
    }

    public YsProProduct getProduct() {
        return product;
    }

    public YsProTime getFrom() {
        return from;
    }

    public YsProTime getTo() {
        return to;
    }

//    public void setTo(YsProTime to) {
//        this.to = to;
//    }

    public String getUuid() {
        return uuid;
    }

    public String getStatus() {
        return status;
    }

    public String getOrderForSame() {
        return orderForSame;
    }

    public String getCustomer() {
        return customer;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public String get(String key){
        return getProperties().get(key);
    }
    public String set(String key,String value){
        return getProperties().put(key, value);
    }

    public String getErrorMessage() {
        return errorMessage.length()==0?null:errorMessage.toString();
    }

    public StoreProduct(@Nullable Integer pos, JsonObject json) {
        this.pos=pos;
        for(Map.Entry<String,JsonElement> one:json.entrySet()) {
            if(PRODUCT_ID_YSPRO.equals(one.getKey())){
                product=YsProProduct.create(one.getValue().getAsString());
            } else if(POS_KEY.equals(one.getKey())) {
                this.pos=one.getValue().getAsInt();
            } else if(UUID_KEY.equals(one.getKey())) {
                uuid=one.getValue().getAsString();
            } else if(FROM_DATE_KEY.equals(one.getKey())) {
                from=YsProTime.create(one.getValue().getAsString());
            } else if(TO_DATE_KEY.equals(one.getKey())) {
                to=YsProTime.create(one.getValue().getAsString());
            } else if(STATUS_KEY.equals(one.getKey())) {
                status=one.getValue().getAsString();
            } else if(ORDER_4_SAME.equals(one.getKey())) {
                orderForSame=one.getValue().getAsString();
            } else if(CUSTOMER_NO.equals(one.getKey())){
                customer = one.getValue().getAsString();
            }
            else {
                properties.put(one.getKey(),one.getValue().getAsString());
            }
        }
    }
    public StoreProduct(JsonObject json) {
        this(null,json);
    }

//    public StoreProduct(List<JsonObject> elements) {
//        for (JsonObject element:elements){
//            String key=element.get("DataName").getAsString();
//            String value=element.get("Value").getAsString();
//            properties.put(key,value);
//            if(uuid==null){
//                uuid=element.get(UUID_KEY).getAsString();
//                JsonElement p=element.get(PRODUCT_ID_YSPRO);
//                if(p!=null)product=YsProProduct.create(p.getAsString());
//                from=YsProTime.create(element.get(FROM_DATE_KEY).getAsString());
//                to=YsProTime.create(element.get(TO_DATE_KEY).getAsString());
//            }
//        }
//    }

    public boolean hasSignal(YsProTime now){
        return now.between(getFrom(),getTo());
    }
    
    public boolean hasSignal(){
        return new YsProTime(new Date()).between(getFrom(),getTo());
    }

    public JsonElement printJson() {
        JsonObject res=new JsonObject();
        if(getPos()!=null)res.addProperty(POS_KEY,getPos());
        if(getProduct()!=null)res.addProperty(PRODUCT_ID_YSPRO,getProduct().toString());
        if(getUuid()!=null)res.addProperty(UUID_KEY,getUuid());
        if(getFrom()!=null)res.addProperty(FROM_DATE_KEY,getFrom().toString());
        if(getTo()!=null)res.addProperty(TO_DATE_KEY,getTo().toString());
        if(getStatus()!=null)res.addProperty(STATUS_KEY,getStatus());
        if(getOrderForSame()!=null)res.addProperty(ORDER_4_SAME,getOrderForSame());
        for(String key:getProperties().keySet()){
            res.addProperty(key, getProperties().get(key));
        }
        return res;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("{\"").append(PRODUCT_ID_YSPRO).append("\":\"").append(getProduct()).append('"');
        if (from != null) sb.append(", \"from\":").append(getFrom());
        if (to != null) sb.append(", \"to\":").append(getTo());
        if (uuid != null) sb.append(", \"uuid\":\"").append(getUuid()).append('"');
        if (properties != null) sb.append(", \"properties\":").append(getProperties());
        if (getErrorMessage() != null) sb.append(", \"errorMessage\":").append(getErrorMessage());
        sb.append('}');
        return sb.toString();
    }
}
