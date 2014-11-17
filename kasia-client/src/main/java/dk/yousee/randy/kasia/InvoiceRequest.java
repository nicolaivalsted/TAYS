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

    public static final String SALES_ITEM_RENT_MOVIE = "2800001";
    public static final String SALES_ITEM_BUY_CLIPCARD = "2800002";
    public static final String SALES_ITEM_BUY_MOVIE = "2800003";

    private String customer;
    private String salesItem;
    private String title;
    private String user;
    private String system;
    private String vodId;
    private String price;
    private String priceNoKoda;
    private String koda;
    private String klientFunktion;
    private String salgskanal;

    /**
     * Old original constructor for YouBio invoices.
     */
    public InvoiceRequest(String customer, String salesItem, String title, String user, String system, String vodId) {
        this.customer = customer;
        this.salesItem = salesItem;
        this.title = title;
        this.user = user;
        this.system = system;
        this.vodId = vodId;
        this.klientFunktion = "rent-movie";
        this.salgskanal = "K";
    }

    /**
     * Constructor which takes all properties - to be used via static named constructors for better readability.
     */
    protected InvoiceRequest(String customer, String salesItem, String title, String user, String system, String vodId, String price, String priceNoKoda, String koda, String klientFunktion, String salgskanal) {
        this.customer = customer;
        this.salesItem = salesItem;
        this.title = title;
        this.user = user;
        this.system = system;
        this.vodId = vodId;
        this.price = price;
        this.priceNoKoda = priceNoKoda;
        this.koda = koda;
        this.klientFunktion = klientFunktion;
        this.salgskanal = salgskanal;
    }

    public static InvoiceRequest createMovieWithPrice(String customer, String salesItem, String title, String user, String system, String vodId, String price, String priceNoKoda, String koda, String klientFunktion) {
        return new InvoiceRequest(customer, salesItem, title, user, system, vodId, price, priceNoKoda, koda, klientFunktion, "W");
    }

    public static InvoiceRequest createBuyClipcardWithPrice(String customer, String salesItem, String user, String system, String price, String klientFunktion) {
        // no "title" nor "vodk-id" for this..
        return new InvoiceRequest(customer, salesItem, null, user, system, null, price, null, null, klientFunktion, "W");
    }

    public JsonObject printJson() {
        JsonObject order = new JsonObject();
        order.addProperty("kundeid", customer);
        
        JsonArray handlinger = new JsonArray();
        JsonObject handling = new JsonObject();
        handling.addProperty("handling", "OPRET");
        handling.addProperty("varenr", salesItem);

        // "title" and "vodk-id" not present on buy-clipcard
        if (title != null && !"".equals(title)) {
            handling.addProperty("title", title);
        }
        if (vodId != null && !"".equals(vodId)) {
            handling.addProperty("vodk-id", vodId);
        }

        // old bio orders did not include a price directly
        if (price != null && !"".equals(price)) {
            if (priceNoKoda != null && koda != null) {
                //
                // Blockbuster is setting KODA and KASIA then wants price to be
                // the price including VAT but without KODA.
                //
                handling.addProperty("pris", priceNoKoda);
                handling.addProperty("koda", koda);
            } else {
                //
                // When KODA hasn't been set explicitly, pass the list price on
                // (aka: Someone else is calculating KODA, somewhere else, probably)
                //
                handling.addProperty("pris", price);
            }
        }

        handlinger.add(handling);
        
        order.add("handlinger", handlinger);
        
        JsonObject info = new JsonObject();
        info.addProperty("salgskanal", salgskanal);
        info.addProperty("klient-funktion", klientFunktion);
        info.addProperty("klient-bruger", user);
        info.addProperty("klient-system", system);
        order.add("info", info);
        
        return order;
    }
}
