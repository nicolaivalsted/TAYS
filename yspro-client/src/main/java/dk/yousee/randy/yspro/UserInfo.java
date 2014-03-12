package dk.yousee.randy.yspro;

import com.google.gson.JsonElement;

/**
 * Information for a user
 */
public class UserInfo extends ProStoreResponse {
    public UserInfo(String json) {
        super(json);
    }

    public String getUserName() {
        return super.getDataArray().get(0).getAsJsonObject().get("UserLogin").getAsString();
    }

    public String getCustomer() {
        return super.getDataArray().get(0).getAsJsonObject().get("CustomerNumber").getAsString();
    }

    /**
     * The same UserID as queried about
     *
     * @return userId
     */
    public String getUserId() {
        return super.getDataArray().get(0).getAsJsonObject().get("UserID").getAsString();
    }

    /**
     * @return true when customer is dibs enabled means has a payment card
     */
    public boolean isDibs() {
        return super.getDataArray().get(0).getAsJsonObject().get("DIBS").getAsBoolean();
    }

    /**
     * 
     * @return email adresse or null if nothing defined
     */
    public String getEmail() {       
        JsonElement res = super.getDataArray().get(0).getAsJsonObject().get("EmailAddress");
        if(res!=null)
            return res.getAsString();   
        return null;
    }
    
    public String getFirstName() {
        return super.getDataArray().get(0).getAsJsonObject().get("FirstName").getAsString();
    }
    
    public String getLastName() {
        return super.getDataArray().get(0).getAsJsonObject().get("LastName").getAsString();
    }
    
    public String getFirstTimeUsed() {
        return super.getDataArray().get(0).getAsJsonObject().get("FirstTimeUsed").getAsString();
    }
    
    @Override
    public boolean isExists() {
        return super.getDataArray().size()>0;
    }
}
