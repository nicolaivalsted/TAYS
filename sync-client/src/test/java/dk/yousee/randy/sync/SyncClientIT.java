package dk.yousee.randy.sync;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.URL;
import java.util.logging.Logger;

/**
 * User: aka
 * Date: 11/01/13
 * Test that sync client works
 */
public class SyncClientIT {

    private final static Logger logger = Logger.getLogger(SyncClientIT.class.getName());

    private SyncClient client = null;

    @Before
    public void setUp() {
        SyncConnectorImpl connector = new SyncConnectorImpl();
        connector.setSyncHost(SyncConnectorImpl.DEV_HOST);
        connector.setSyncHost(SyncConnectorImpl.T_HOST);
//        connector.setSyncHost(SyncConnectorImpl.DEV_HOST);
        client = new SyncClient();
        client.setConnector(connector);
    }

    @Test
    public void notProd() throws Exception {
        Assert.assertNotSame(SyncConnectorImpl.P_HOST, client.getConnector().getSyncHost());
    }

    @Test
    public void generateCreateUrl() throws Exception {
        URL url=client.generateCreateUrl();
        Assert.assertNotNull(url);
        Assert.assertTrue(url.toExternalForm().startsWith(client.getConnector().getSyncHost()));
        logger.info("URL: "+url);
    }

    @Test
    public void createPlayEvent() throws Exception {
        String subscriber="608301280";
        String serviceItem="1301018";
        boolean signal=true;
        CreatePlayRequest request=new CreatePlayRequest(subscriber,serviceItem,signal
            ,"sync-client-it","aftalenr","k32444");
        SyncResponse response=client.createPlayEvent(request);
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getSubscriber());
    }
    @Test
    public void processPlayEvent() throws Exception {
        SubscriberId subscriber=new SubscriberId("608301280");
        String serviceItem="1301018";
        boolean signal=true;
        CreatePlayRequest request=new CreatePlayRequest(subscriber,serviceItem,signal
            ,"sync-client-it","aftalenr","k32444");
        SyncResponse response=client.createPlayEvent(request);
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getSubscriber());

        SyncResponse response2 = client.processPlayEvent(subscriber, response.getProcessLink());
        Assert.assertNotNull(response2);
        Assert.assertNotNull(response2.getError());
        Assert.assertNotNull(response2.getMessage());

    }
}
