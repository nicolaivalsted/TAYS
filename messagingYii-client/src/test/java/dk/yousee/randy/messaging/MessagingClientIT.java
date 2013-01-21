package dk.yousee.randy.messaging;

import com.google.gson.JsonObject;
import dk.yousee.randy.base.AbstractConnector;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    public void before(){
        AbstractConnector ac = new AbstractConnector() {};
        client = new MessagingClient(PRE_PROD_HOST, ACCEPT_HEADER, ac);       
    }
    
    @AfterClass
    public void after(){
        client.getConnector().destroy();
    }
    
    @Test
    public void createMail(){
        try {
            JsonObject data = new JsonObject();
            
            client.createMessagingOrder("608252633", MessagingKontaktForm.RandyLukWebmail, "sikj@yousee.dk", data);
        } catch (MessagingException ex) {
            Assert.fail(ex.getMessage());
        }
        
    }
    
    
}
