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
public class InvoiceResponse {

    private JsonElement jsonSource;
    private String message;
    private OrderOutput orderOutput;

    public InvoiceResponse(String message, String kasiaResponse) {
        this.message = message == null ? null : (message.trim().length() == 0 ? null : message);
        if (kasiaResponse != null && kasiaResponse.trim().length() > 0) {
            try {
                jsonSource = new JsonParser().parse(kasiaResponse);
                JsonObject root = jsonSource.getAsJsonObject();

                String name = "order-output";
                JsonElement element = root.get(name);
                if (element != null) {
                    orderOutput = new OrderOutput(element.getAsJsonObject());
                } else {
                    this.message="Kasia response does not contain json element "+name +" k2-error: " + jsonSource.toString();
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

    /**
     * Qualified parsed response from Kasia
     * @return instance that contains order id
     */
    public OrderOutput getOrderOutput() {
        return orderOutput;
    }

    public class OrderOutput {
        private String uuid;

        public OrderOutput(JsonObject jsonObject) {
            uuid = jsonObject.get("uuid").getAsString();
        }

        /**
         * UUID that describes the order number
         * @return uuid
         */
        public String getUuid() {
            return uuid;
        }
    }
}
