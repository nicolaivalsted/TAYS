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
 * Date: 11/01/13
 * Time: 15.03
 * Contains the response from Item Query
 */
public class ItemResponse {

    String input;
    List<ItemRow> rows;
    String error;
    String message;

    private Date readTime;


    public ItemResponse(String input) {
        this.readTime = new Date();
        this.input = input;
        JsonElement jsonSource;
        try {
            jsonSource = new JsonParser().parse(input);
            rows = parse(jsonSource.getAsJsonArray());
        } catch (JsonSyntaxException e) {
            rows = initList();
            this.message = String.format("Tried to parse service-map response, got syntax error, message: %s", e.getMessage());
        }
    }
//    public ItemResponse(ItemResponse all,String filerAnlaeg) {
//
//        this.readTime = all.getReadTime();
//        this.input = all.getInput();
//        this.error=all.error;
//        this.message=all.message;
//        rows=new ArrayList<MailRow>();
//        for(MailRow row:all.getRows()){
//            if(row.getAnlaeg().equals(filerAnlaeg)){
//                rows.add(row);
//            }
//        }
//    }

    public ItemResponse(String error, String message) {
        this.error = error;
        this.message = message;
        rows = initList();
    }

    private List<ItemRow> parse(JsonArray array) {
        List<ItemRow> res = initList();
        for (JsonElement element : array) {
            ItemRow row = new ItemRow(element.getAsJsonObject());
            res.add(row);
        }
        return res;
    }

    private List<ItemRow> initList() {
        return new ArrayList<ItemRow>();
    }

    public Date getReadTime() {
        return readTime;
    }

    public String getInput() {
        return input;
    }

    public List<ItemRow> getRows() {
        return rows;
    }

    public String getMessage() {
        return message;
    }
}
