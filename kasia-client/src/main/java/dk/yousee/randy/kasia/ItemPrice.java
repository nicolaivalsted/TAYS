package dk.yousee.randy.kasia;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.Nullable;

/**
* Created with IntelliJ IDEA.
* User: aka
* Date: 28/10/12
* Time: 12.00
* Price for an item - read from kasia II
*/
public class ItemPrice {
    private String id;
    private JsonObject json;

    public ItemPrice(String id, JsonObject json) {
        this.id = id;
        this.json = json;
    }

    public String getId() {
        return id;
    }

    /**
     * Gets the price from Kasia II
     * @return a
     */
    public String getTotalpris() {
        return json == null ? null : json.get("totalpris").getAsString();
    }

    public int getFejlkode() {
        if(json==null){
            return 0;
        } else {
            JsonElement element = json.get("fejlkode");
            if(element==null){
                return 0;
            } else {
                return element.getAsInt();
            }
        }
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("{\"id\":\"").append(id).append('"');
        if (getTotalpris() != null) sb.append(", \"totalPris\":").append('"').append(getTotalpris()).append('"');
        if (getFejlkode() != 0) sb.append(", \"error\":").append(getFejlkode());
        sb.append('}');
        return sb.toString();
    }

    static String centiKroner(@Nullable String price){
        if(price==null || price.trim().length()==0){
            return "000";
        } else {
            price=price.trim();
            int posDot=price.indexOf('.');
            if(posDot!=-1){
                return centiKroner(posDot,price);
            } else {
                int posComma=price.indexOf(',');
                if(posComma!=-1) {
                    return centiKroner(posComma,price);
                } else {
                    return price+"00";
                }
            }
        }
    }

    private static String centiKroner(int posDecimal, String price) {
        StringBuilder sb=new StringBuilder();
        // kroner
        if(posDecimal==0) {
            sb.append("0");
        } else {
            sb.append(price.substring(0, posDecimal));
        }
        //centi
        if((posDecimal+1)==price.length()){
            sb.append("00");
        } else {
            String centi;
            centi=price.substring(posDecimal+1);
            sb.append(centi);
            if(centi.length()==1){
                sb.append("0");
            }
        }
        return sb.toString();
    }

    public boolean matchPrice(String price) {
        return centiKroner(this.getTotalpris()).equals(centiKroner(price));
    }
}
