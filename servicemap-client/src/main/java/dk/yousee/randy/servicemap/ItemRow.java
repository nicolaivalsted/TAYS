package dk.yousee.randy.servicemap;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: aka
 * Date: 20/11/12
 * Time: 21.33
 * One Item mapping
 */
public class ItemRow {

    private Map<String,String> map=new HashMap<String,String>();

    public ItemRow(JsonObject node) {

        Set<Map.Entry<String,JsonElement>> entries = node.entrySet();
        for(Map.Entry<String,JsonElement> one: entries){
            map.put(one.getKey(),one.getValue().getAsString());
        }
    }
    public Map<String, String> getMap() {
        return map;
    }

    public String getStalone() {
        String st=map.get("stalone");
        if(st==null){
            st=map.get("item");
        }
        return st;
    }
    public String getPlan() {
        String res;
        res=map.get("plan");
        return res;
    }

    public String getDescription() {
        String res;
        res=map.get("description");
        return res;
    }
    public String getOttProduct() {
        String res;
        res=map.get("ottProduct");
        return res;
    }

    public JsonElement printJson() {
        JsonObject one=new JsonObject();
        for (String key : map.keySet()) {
            if("plan-id".equals(key)) {
                one.addProperty(key,Long.parseLong(map.get(key)));
            } else {
                one.addProperty(key,map.get(key));
            }
        }
        return one;
    }

}