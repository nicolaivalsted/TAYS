package dk.yousee.randy.yspro;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: aka
 * Date: 10/09/12
 * Time: 15.45
 * List of products to write to
 */
public class WriteList {


    private String customer;
    private Date writeTime;
    private List<WriteProduct> products=new ArrayList<WriteProduct>();

    public WriteList(String customer, Date writeTime) {
        this.customer = customer;
        this.writeTime = writeTime;
    }

    public String getCustomer() {
        return customer;
    }

    public JsonElement printJson() {

        JsonArray array = new JsonArray();
        for (WriteProduct product : products) {
            array.add(product.printJson());
        }
        return array;
    }

    public void add(Action action, StoreProduct p){
        WriteProduct wp=new WriteProduct(action,p);
        products.add(wp);
    }

    public boolean isEmpty() {
        return products.isEmpty();
    }
    
    public List<WriteProduct> getProducts() {
		return products;
	}

    public enum Action {
        add,
        update,
        delete
    }

    private static final YsProTime endlessTime=YsProTime.create("9999-12-31 23:59:59");
    public class WriteProduct {
        Action action;
        StoreProduct product;

        public WriteProduct(Action action, StoreProduct product) {
            this.action = action;
            this.product = product;
        }
        
        public Action getAction() {
			return action;
		}
        
        public StoreProduct getProduct() {
			return product;
		}

        public JsonElement printJson() {
            JsonObject res=product.printJson().getAsJsonObject();
            res.remove(StoreProduct.POS_KEY);
            if(action== Action.add){
                res.remove(StoreProduct.UUID_KEY);
                res.addProperty(StoreProduct.FROM_DATE_KEY, YsProTime.midNight(writeTime).toString());
                res.addProperty(StoreProduct.TO_DATE_KEY,endlessTime.toString());
            }
            if(action== Action.delete){
                res.addProperty(StoreProduct.TO_DATE_KEY, YsProTime.createFromLastHour().toString());
            }
            return res;
        }
    }
}
