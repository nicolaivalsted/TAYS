package dk.yousee.randy.dms;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.logging.Logger;

/**
 * User: aka
 * Date: 06/11/12
 * Time: 08.56
 * Test that Service Map client works
 */
public class DmsClientIT {

    private final static Logger logger = Logger.getLogger(DmsClientIT.class.getName());

    private DmsClient client = null;

    @Before
    public void setUp() {
        DmsConnectorImpl connector = new DmsConnectorImpl();
        connector.setDmsHost(DmsConnectorImpl.DEV_HOST);
        connector.setDmsHost(DmsConnectorImpl.T_HOST);
        client = new DmsClient();
        client.setConnector(connector);
    }

    @Test
    public void constructResponse(){
        String customer="608301280";
        EventResponse response=new EventResponse(customer,true,"{\"message\":\"ok\"}");
        Assert.assertNotNull(response);
        Assert.assertEquals("ok",response.getMessage());
        Assert.assertTrue(response.isSuccess());
    }

    @Test
    public void postEvent() throws Exception {
        Assert.assertNotSame(DmsConnectorImpl.P_HOST, client.getConnector().getDmsHost());
        String customer="608301280";
        logger.info("URL: " + client.generateEventUrl(customer));
        EventResponse response = client.postEvent(customer);
        Assert.assertNotNull(response);
        Assert.assertEquals(customer, response.getCustomer());
        Assert.assertNotNull(response.getReadTime());
        Assert.assertNotNull(response.getMessage());
        Assert.assertNotNull(response.printJson());
    }

    @Test
    public void badHost() throws Exception {
        client.getConnector().setDmsHost("http://no.horse.cow:9999");
        String customer="608301280";
        logger.info("URL: " + client.generateEventUrl(customer));
        EventResponse response = client.postEvent(customer);
        Assert.assertNotNull(response);
        Assert.assertEquals(customer, response.getCustomer());
        Assert.assertFalse(response.isSuccess());
    }
    @Test
    public void badCustomer() throws Exception {
        Assert.assertNotSame(DmsConnectorImpl.P_HOST, client.getConnector().getDmsHost());
        String customer="111000999";
        logger.info("URL: " + client.generateEventUrl(customer));
        EventResponse response = client.postEvent(customer);
        Assert.assertNotNull(response);
        Assert.assertTrue(response.isSuccess()); // TODO HERE I WOULD LIKE A FALSE ???
    }
}
