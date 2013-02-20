package dk.yousee.randy.sync;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

/**
 * User: aka
 * Date: 12/01/13
 * Time: 07.41
 * Response from creating pm2 engagement
 */
public class CreatePmResponse {

    SubscriberId subscriber;
    String input;

    String error;
    String message;

    String pmId;



    public CreatePmResponse(SubscriberId subscriber, String error, String message) {
        this.subscriber = subscriber;
        this.error = error;
        this.message = message;
    }

    public CreatePmResponse(SubscriberId subscriber, String input) {
        this.subscriber = subscriber;
        this.input = input;
        JsonElement jsonSource;
        try {
            jsonSource = new JsonParser().parse(input);
            pmId =jsonSource.getAsJsonObject().get("pm_id").getAsString();
            JsonElement er = jsonSource.getAsJsonObject().get("error");
            if(er!=null){
                error=er.getAsString();
            }
            JsonElement msg = jsonSource.getAsJsonObject().get("message");
            if(msg!=null){
                message=msg.getAsString();
            }
        } catch (JsonSyntaxException e) {
            this.error="parseError";
            this.message = String.format("Tried to parse sync response, got syntax error, message: %s", e.getMessage());
        }
    }


    public SubscriberId getSubscriber() {
        return subscriber;
    }

    public String getPmId() {
        return pmId;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

}
