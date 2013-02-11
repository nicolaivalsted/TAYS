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
        productCode =node.get("product_code").getAsString();
        productName =node.get("product_name").getAsString();
        subPos=node.get("sub_pos").getAsString();
    }

    private String anlaeg;

    public String getAnlaeg() {
        return anlaeg;
    }
    /**
     * Key to foreningsmail
     */
    private String productCode;

    public String getProductCode() {
        return productCode;
    }

    /**
     * Description of forenings mail
     */
    private String productName;

    public String getProductName() {
        return productName;
    }


    private String subPos;

    public String getSubPos() {
        return subPos;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("{\"anlaeg\":\"").append(getAnlaeg()).append('"');
        sb.append(", \"product_code\":\"").append(getProductCode()).append('"');
        sb.append(", \"product_name\":\"").append(getProductName()).append('"');
        sb.append(", \"sub_pos\":\"").append(getSubPos()).append('"');
        sb.append('}');
        return sb.toString();
    }
}
