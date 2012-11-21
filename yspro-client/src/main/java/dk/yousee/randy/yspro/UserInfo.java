package dk.yousee.randy.yspro;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.Map;
import java.util.TreeMap;

/**
 * Information for a user
 */
public class UserInfo {

    private String input;

    private Map<String,String> map;

    private int status;
    private String message;

    /**
     * Status of read
     * @return 0=success, other value is error
     */
    public int getStatus() {
        return status;
    }

    /**
     * Message returned from YsPro
     * @return string "OK" or an error
     */
    public String getMessage() {
        return message;
    }

    public String getCustomer() {
        return getMap().get("CustomerNumber");
    }

    /**
     * The same UserID as queried about
     * @return userId
     */
    public String getUserId() {
        return getMap().get("UserID");
    }

    /**
     * @return true when customer is dibs enabled means has a payment card
     */
    public boolean isDibs() {
        return "1".equals(getMap().get("DIBS"));
    }

    public String getInput() {
        return input;
    }

    public Map<String, String> getMap() {
        return map;
    }

    public UserInfo(int status, String message) {
        this.status = status;
        this.message = message;
        map=new TreeMap<String, String>();
        input="no input read";
    }

    public UserInfo(DataFormat format, String input) {
        this.input = input;
        if(format==DataFormat.xml){
            map=parseJson(parseXml(input));
            String stat=filterXml("Status",input);
            if(stat!=null){
                status=Integer.decode(stat);
            }
            message=filterXml("Message",input);
        } else if(format==DataFormat.json){
            map=parseJson(input);
        } else {
            throw new IllegalArgumentException(String.format("format %s is not allowed",format));
        }
    }

    private Map<String, String> parseJson(String input) {
        JsonElement jsonSource;
        jsonSource = new JsonParser().parse(input);
        return parseJson(jsonSource);
    }

    private Map<String, String> parseJson(JsonElement jsonSource) {
        JsonObject jo=jsonSource.getAsJsonObject();
        Map<String,String> res=new TreeMap<String, String>();
        for(Map.Entry<String, JsonElement> entry:jo.entrySet()){
            res.put(entry.getKey(),entry.getValue().getAsString()   );
        }
        return res;
    }

    private JsonObject parseXml(String xml) {

        JsonObject jo = new JsonObject();
        addProperty("UserID", jo, xml);
        addProperty("FirstName", jo, xml);
        addProperty("LastName", jo, xml);
        addProperty("EmailAddress", jo, xml);
        addProperty("CellPhone", jo, xml);
        addProperty("Address1", jo, xml);
        addProperty("Address2", jo, xml);
        addProperty("Zipcode", jo, xml);
        addProperty("City", jo, xml);
        addProperty("Country", jo, xml);
        addProperty("CustomerNumber", jo, xml);
        addProperty("KPMNumber", jo, xml);
        addProperty("UserLogin", jo, xml);
        addProperty("TDCExternalPersonID", jo, xml);
        addProperty("FirstTimeUsed", jo, xml);
        addProperty("IsActive", jo, xml);
        addProperty("DIBS", jo, xml);
        return jo;
    }

    private void addProperty(String tag, JsonObject jo, String xml) {
        String value=filterXml(tag, xml);
        if(value!=null){
            jo.addProperty(tag, value);
        }
    }

    private String filterXml(String tag, String xml) {
        String res=null;
        int pos0 = xml.indexOf("<" + tag + ">");
        int pos1 = xml.indexOf("</" + tag + ">");
        if (pos0 > 0 && pos1 > 0) {
            int tagLen = tag.length() + 2;
            String value = xml.substring(pos0 + tagLen, pos1);
            if (value != null && value.trim().length() != 0) {
                res=value;
            }
        }
        return res;
    }


    public JsonElement printJson() {
//        JSONObject.fromObject(map);
        JsonObject jo=new JsonObject();
        if(status!=0){
            jo.addProperty("Status",getStatus());
            jo.addProperty("Message",getMessage());
        }
        for (String one:map.keySet()) {
            jo.addProperty(one, map.get(one));
        }
        return jo;
    }

    public static enum DataFormat {
        xml,
        json
    }
}
