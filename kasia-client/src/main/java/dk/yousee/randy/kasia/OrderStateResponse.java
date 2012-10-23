package dk.yousee.randy.kasia;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

/**
 * User: aka
 * Date: 19/10/12
 * Time: 10.00
 * Response from opening invoice
 */
public class OrderStateResponse {

    private JsonElement jsonSource;
    private String message;
    private String status;

    public OrderStateResponse(String message, String kasiaResponse) {
        this.message = message == null ? null : (message.trim().length() == 0 ? null : message);
        if (kasiaResponse != null && kasiaResponse.trim().length() > 0) {
            try {
                jsonSource = new JsonParser().parse(kasiaResponse);
                JsonObject root = jsonSource.getAsJsonObject();

                String name = "status";
                JsonElement element = root.get(name);
                if (element != null) {
                    status=element.getAsString();
                } else {
                    this.message=String.format("Kasia response does not contain json element %s ",name);
                }
                if(message==null && root.has("fejl")){
                    this.message=root.get("fejl").getAsString();
                }
            } catch (JsonSyntaxException e) {
                this.message = String.format("Tried to parse kasia response, got syntax error, message: %s", e.getMessage());
            }
        }
    }

    public JsonElement getJsonSource() {
        return jsonSource;
    }

    /**
     * @return not null string means problems !!!!
     */
    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }
}
