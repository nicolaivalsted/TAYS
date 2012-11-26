package dk.yousee.randy.servicemap;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: aka
 * Date: 20/11/12
 * Time: 21.49
 * Response from query of forenings mails
 */
public class MailResponse {

    String input;
    List<MailRow> rows;
    String error;
    String message;

    private Date readTime;


    public MailResponse(String input) {
        this.readTime = new Date();
        this.input = input;
        JsonElement jsonSource;
        try {
            jsonSource = new JsonParser().parse(input);
            rows = parse(jsonSource.getAsJsonArray());
        } catch (JsonSyntaxException e) {
            rows = initVendors();
            this.message = String.format("Tried to parse service-map response, got syntax error, message: %s", e.getMessage());
        }
    }
    public MailResponse(MailResponse all,String filerAnlaeg) {

        this.readTime = all.getReadTime();
        this.input = all.getInput();
        this.error=all.error;
        this.message=all.message;
        rows=new ArrayList<MailRow>();
        for(MailRow row:all.getRows()){
            if(row.getAnlaeg().equals(filerAnlaeg)){
                rows.add(row);
            }
        }
    }

    public MailResponse(String error, String message) {
        this.error = error;
        this.message = message;
        rows = initVendors();
    }

    private List<MailRow> parse(JsonArray array) {
        List<MailRow> res = initVendors();
        for (JsonElement element : array) {
            MailRow vendor = new MailRow(element.getAsJsonObject());
            res.add(vendor);
        }
        return res;
    }

    private List<MailRow> initVendors() {
        return new ArrayList<MailRow>();
    }

    public Date getReadTime() {
        return readTime;
    }

    public String getInput() {
        return input;
    }

    public List<MailRow> getRows() {
        return rows;
    }

    public String getMessage() {
        return message;
    }

//    /**
//     * Filter the isp to get.
//     * @param isp null matches a row with null in value, other value matches a row with that value
//     * @return vendor that matches isp
//     */
//    public ForeningsMailRow filterByIsp(@Nullable String isp) {
//        for(ForeningsMailRow vendor: rows){
//            if(vendor.matchIsp(isp)){
//                return vendor;
//            }
//        }
//        return null;
//    }

}
