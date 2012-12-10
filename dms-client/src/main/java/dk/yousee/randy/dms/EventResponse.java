package dk.yousee.randy.dms;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.util.Date;

/**
 * User: aka
 * Date: 20/11/12
 * Time: 21.49
 * Response from sending event
 */
public class EventResponse {

    String customer;
    String message;
    boolean success;

    private Date readTime= new Date();


    public EventResponse(String customer,boolean success,String input) {
        this.customer=customer;
        this.success=success;
        JsonElement jsonSource;
        try {
            jsonSource = new JsonParser().parse(input);
            if(jsonSource.isJsonObject()){
                message=jsonSource.getAsJsonObject().get("message").getAsString();
            } else {
                message=String.format("could not parse input: %s",input);
                this.success=false;
            }
        } catch (JsonSyntaxException e) {
            this.message = String.format("Tried to parse event response, got syntax error, message: %s", e.getMessage());
        }
    }

    public EventResponse(String customer, String message) {
        this.customer = customer;
        this.message = message;
        this.success=false;
    }

    public String getCustomer() {
        return customer;
    }

    public Date getReadTime() {
        return readTime;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }
}
