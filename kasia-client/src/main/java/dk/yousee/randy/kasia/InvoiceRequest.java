package dk.yousee.randy.kasia;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * User: aka
 * Date: 19/10/12
 * Time: 09.59
 * Request for invoice
 */
public class InvoiceRequest {

    private String customer;
    private String salesItem;
    private String title;
    private String user;
    private String system;
    private String vodId;

    public InvoiceRequest(String customer, String salesItem, String title, String user, String system, String vodId) {
        this.customer = customer;
        this.salesItem = salesItem;
        this.title = title;
        this.user = user;
        this.system = system;
        this.vodId = vodId;
    }

    public JsonObject printJson() {
        String res;
        res = String.format(
            "{  \"kundeid\" : \"" + customer + "\",\n" +
                "        \"handlinger\" : [{\n" +
                "        \"handling\" : \"OPRET\",\n" +
                "            \"varenr\" : \"" + salesItem + "\",\n" +
                "            \"title\" : \"" + title + "\"\n" +
                "    }],\n" +
                "        \"info\" : {\n" +
                "            \"salgskanal\" : \"K\",\n" +
                "            \"klient-funktion\" : \"rent-movie\",\n" +
                "            \"klient-bruger\" : \"" + user + "\",\n" +
                "            \"klient-system\" : \"" + system + "\"\n" +
                "    }\n" +
                "}\n");
        JsonObject order = new JsonObject();       
        order.addProperty("kundeid", customer);
        
        JsonArray handlinger = new JsonArray();
        JsonObject handling = new JsonObject();
        handling.addProperty("handling", "OPRET");
        handling.addProperty("varenr", salesItem);
        handling.addProperty("title", title);
        handling.addProperty("vodk-id", vodId);
        handlinger.add(handling);
        
        order.add("handlinger", handlinger);
        
        JsonObject info = new JsonObject();
        info.addProperty("salgskanal", "K");
        info.addProperty("klient-funktion", "rent-movie");
        info.addProperty("klient-bruger", user);
        info.addProperty("klient-system", system);
        order.add("info", info);
        
        return order;
    }
}
