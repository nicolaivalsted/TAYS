package dk.yousee.randy.servicemap;

import com.google.gson.JsonObject;

/**
 * Created with IntelliJ IDEA.
 * User: aka
 * Date: 20/11/12
 * Time: 21.33
 * One forenings mail entity
 */
public class MailRow {


    public MailRow(JsonObject node) {
        anlaeg=node.get("anlaeg").getAsString();
        product=node.get("product").getAsString();
        name=node.get("name").getAsString();
        subPos=node.get("sub_pos").getAsString();
    }

//    private String parse(JsonObject node, String key) {
//        JsonElement element=node.get(key);
//        String res;
//        if(element==null){
//            res=null;
//        } else {
//            res=element.getAsString();
//        }
//        return res;
//    }

    private String anlaeg;

    public String getAnlaeg() {
        return anlaeg;
    }

    private String product;

    public String getProduct() {
        return product;
    }


    private String name;

    public String getName() {
        return name;
    }

    private String subPos;

    public String getSubPos() {
        return subPos;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("{\"anlaeg\":\"").append(getAnlaeg()).append('"');
        sb.append(", \"product\":\"").append(getProduct()).append('"');
        sb.append(", \"name\":\"").append(getName()).append('"');
        sb.append(", \"subPos\":\"").append(getSubPos()).append('"');
        sb.append('}');
        return sb.toString();
    }
}
