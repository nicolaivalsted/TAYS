package dk.yousee.randy.servicemap;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: aka
 * Date: 19/11/12
 * Time: 18.25
 * Result of reading vendors from service map service
 */
public class Vendors {

    String input;
    List<Vendor> vendors;
    String error;
    String message;

    private Date readTime;


    public Vendors(String input) {
        this.readTime = new Date();
        this.input = input;
        JsonElement jsonSource;
        try {
            jsonSource = new JsonParser().parse(input);
            vendors = parse(jsonSource.getAsJsonArray());
        } catch (JsonSyntaxException e) {
            vendors = initVendors();
            this.message = String.format("Tried to parse service-map response, got syntax error, message: %s", e.getMessage());
        }
    }

    public Vendors(String error, String message) {
        this.error = error;
        this.message = message;
        vendors = initVendors();
    }

    private List<Vendor> parse(JsonArray array) {
        List<Vendor> res = initVendors();
        for (JsonElement element : array) {
            Vendor vendor = new Vendor(element.getAsJsonObject());
            res.add(vendor);
        }
        return res;
    }

    private List<Vendor> initVendors() {
        return new ArrayList<Vendor>();
    }

    public Date getReadTime() {
        return readTime;
    }

    public String getInput() {
        return input;
    }

    public List<Vendor> getVendors() {
        return vendors;
    }

    public String getMessage() {
        return message;
    }

    /**
     * Filter the isp to get.
     * @param isp null matches a row with null in value, other value matches a row with that value
     * @return vendor that matches isp
     */
    public Vendor filterByIsp(@Nullable String isp) {
        for(Vendor vendor:vendors){
            if(vendor.matchIsp(isp)){
                return vendor;
            }
        }
        return null;
    }
}
