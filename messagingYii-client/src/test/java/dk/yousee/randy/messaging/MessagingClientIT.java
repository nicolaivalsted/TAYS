package dk.yousee.randy.messaging;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dk.yousee.randy.base.HttpPool;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author m27236
 */
public class MessagingClientIT {

    private static MessagingClient client;
    private static String PRE_PROD_HOST = "http://preprod-messaging.int.yousee.dk/messaging";
    private static String ACCEPT_HEADER = "application/vnd.yousee.messaging;version=0.3;charset-UFT-8";    
    
    @BeforeClass
    public static void before(){
        HttpPool ac = new HttpPool();
        client = new MessagingClient(PRE_PROD_HOST, ACCEPT_HEADER, ac);       
    }
    
    @AfterClass
    public static void after(){
        client.getHttpPool().shutdown();
    }
    
    /**
     * "data": {
    "fornavn": "Simon",
    "efternavn": "Kjær",
    "lukkeDate": "30/2/2013",
    "mailAlias": [{"navn":"cool@MF.dk"},{"navn":"bamf@sexyYouSee.dk"}],
    "lastDate":"20/2/2013"
     */
    @Test
    public void createMail(){
        try {
            JsonObject data = new JsonObject();
            data.addProperty("fornavn", "Simon Oliver Folke");
            data.addProperty("efternavn", "Kjær");
            data.addProperty("lukkeDate", "30/02/2013");
            data.addProperty("lastDate", "20/02/2013");
            JsonArray array = new JsonArray();
            JsonObject jo = new JsonObject();
            jo.addProperty("navn", "sexy@yousee.dk");         
            array.add(jo);
            JsonObject jo2 = new JsonObject();
            jo2.addProperty("navn", "toSexy@webspeed.dk");         
            array.add(jo2);
            data.add("mailAlias", array);
            
            
            String orderId = client.createMessagingOrder("608252633", "RandyLukWebmail", "sikj@yousee.dk", "sikj@yousee.dk", data);
            Assert.assertNotNull(orderId);
        } catch (MessagingException ex) {
            Assert.fail(ex.getMessage());
        }
        
    }
    
    
}
