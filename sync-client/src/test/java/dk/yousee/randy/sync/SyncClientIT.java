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
        String position="1234";
        String serviceItem="1301018";
        boolean signal=true;
        CreatePlayRequest request=new CreatePlayRequest(subscriber,position ,serviceItem,signal
            ,"sync-client-it","aftalenr","k32444");
        SyncResponse response=client.createPlayEvent(request);
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getSubscriber());
    }

    @Test
    public void processPlayEvent() throws Exception {
        SubscriberId subscriber=new SubscriberId("608301280");
        String position="1234";
        String serviceItem="1301018";
        boolean signal=true;
        CreatePlayRequest request=new CreatePlayRequest(subscriber,position,serviceItem,signal
            ,"sync-client-it","aftalenr","k32444");
        SyncResponse response=client.createPlayEvent(request);
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getSubscriber());

        SyncResponse response2 = client.processPlayEvent(subscriber, response.getProcessLink());
        Assert.assertNotNull(response2);
        Assert.assertNotNull(response2.getError());
        Assert.assertNotNull(response2.getMessage());
    }

    private static final String content
        ="[\n" +
        "  {\n" +
        "    \"modemId\": \"106282918\",\n" +
        "    \"rateCodes\": \"1301130\",\n" +
        "    \"signal\": \"true\",\n" +
        "    \"type\": \"CABLEBB\"\n" +
        "  },\n" +
        "  {\n" +
        "    \"modemId\": \"na\",\n" +
        "    \"name\": \"foreningsmail.dk\",\n" +
        "    \"position\": \"106282918.1001\",\n" +
        "    \"product\": \"ystest\",\n" +
        "    \"signal\": \"true\",\n" +
        "    \"type\": \"ForeningsMail\"\n" +
        "  },\n" +
        "  {\n" +
        "    \"aftale\": \"1174496\",\n" +
        "    \"customer\": \"617601483\",\n" +
        "    \"position\": \"-1039618750\",\n" +
        "    \"product-code\": \"1302003\",\n" +
        "    \"signal\": \"true\",\n" +
        "    \"type\": \"Sikkerhedspakken\",\n" +
        "    \"value-date\": \"2013-02-14\"\n" +
        "  },\n" +
        "  {\n" +
        "    \"aftale\": \"1174496\",\n" +
        "    \"customer\": \"617601483\",\n" +
        "    \"position\": \"-1055587549\",\n" +
        "    \"product-code\": \"1302006\",\n" +
        "    \"signal\": \"true\",\n" +
        "    \"type\": \"Backup\",\n" +
        "    \"value-date\": \"2013-02-16\"\n" +
        "  },\n" +
        "  {\n" +
        "    \"aftale\": \"1174496\",\n" +
        "    \"customer\": \"617601483\",\n" +
        "    \"position\": \"-1060349927\",\n" +
        "    \"product-code\": \"1302006\",\n" +
        "    \"signal\": \"true\",\n" +
        "    \"type\": \"Backup\",\n" +
        "    \"value-date\": \"2013-01-09\"\n" +
        "  }\n" +
        "]";


    @Test
    public void createPmEngagement() throws Exception {
        SubscriberId subscriber=new SubscriberId("617601483");
        CreatePmRequest request=new CreatePmRequest(subscriber,"sync-client-it","aftalenr","k32444",content);
        CreatePmResponse response=client.createPmEngagement(request);
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getSubscriber());
        Assert.assertNotNull(response.getPmId());
    }

    /**
     * Test that PM event can be created based on a record
     * @throws Exception on problems
     */
    @Test
    public void createPmEvent() throws Exception {
        SubscriberId subscriber=new SubscriberId("617601483");
        CreatePmRequest request=new CreatePmRequest(subscriber,"sync-client-it","aftalenr","k32444",content);
        Long pmId=1L; // increment this value when needed
        SyncResponse response=client.createPmEvent(request,pmId);
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getSubscriber());
    }
}
