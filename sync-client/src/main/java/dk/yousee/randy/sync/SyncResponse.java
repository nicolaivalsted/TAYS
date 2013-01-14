package dk.yousee.randy.sync;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.List;

/**
 * User: aka
 * Date: 12/01/13
 * Time: 07.41
 * Response from creating play
 */
public class SyncResponse {

    SubscriberId subscriber;
    String input;

    String error;
    String message;


    List<Link> links=new ArrayList<Link>();



    public SyncResponse(SubscriberId subscriber, String error, String message) {
        this.subscriber = subscriber;
        this.error = error;
        this.message = message;

//        rows = initList();
    }

    public SyncResponse(SubscriberId subscriber, String input) {
        this.subscriber = subscriber;
        this.input = input;
        JsonElement jsonSource;
        try {
            jsonSource = new JsonParser().parse(input);
            jsonSource.toString();
            JsonElement jsonLinks = jsonSource.getAsJsonObject().get("links");
            JsonArray jsonArray = jsonLinks.getAsJsonArray();
            links=parseLinks(jsonArray);
            JsonElement ce = jsonSource.getAsJsonObject().get("complete");
            if(ce!=null){
                complete="true".equals(ce.getAsString());
            }
            JsonElement er = jsonSource.getAsJsonObject().get("error");
            if(er!=null){
                error=er.getAsString();
            }
            JsonElement msg = jsonSource.getAsJsonObject().get("message");
            if(msg!=null){
                message=msg.getAsString();
            }
            JsonElement op = jsonSource.getAsJsonObject().get("operations");
            if(op!=null){
                JsonArray ar=op.getAsJsonArray();
                if(ar.size()>0){
                    JsonObject last=ar.get(ar.size()-1).getAsJsonObject();
                    JsonElement er2 = last.getAsJsonObject().get("error");
                    if(er2!=null){
                        error=er2.getAsString();
                    }
                    JsonElement msg2 = last.getAsJsonObject().get("message");
                    if(msg2!=null){
                        message=msg2.getAsString();
                    }
                }
            }


        } catch (JsonSyntaxException e) {
            this.error="parseError";
            this.message = String.format("Tried to parse sync response, got syntax error, message: %s", e.getMessage());
        }
    }

    private List<Link> parseLinks(JsonArray jsonArray){
        List<Link> res=new ArrayList<Link>();
        for(JsonElement element:jsonArray){
            res.add(new Link(element.getAsJsonObject()));
        }
        return res;
    }

    public SubscriberId getSubscriber() {
        return subscriber;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public Link getProcessLink() {
        return getRelLink("process");
    }
    public Link getQueryLink() {
        return getRelLink("query");
    }
    private Link getRelLink(String rel) {
        for(Link link: links){
            if(rel.equals(link.getRel())) {
                return link;
            }
        }
        return null;
    }

    boolean complete;

    public boolean isComplete() {
        return complete;
    }

}
