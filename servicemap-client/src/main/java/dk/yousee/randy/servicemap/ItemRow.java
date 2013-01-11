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
