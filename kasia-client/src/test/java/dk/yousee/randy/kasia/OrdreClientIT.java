package dk.yousee.randy.kasia;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * User: aka
 * Date: 21/10/12
 * Time: 11.35
 * Integration test
 */
public class OrdreClientIT {

    private OrdreClient client;

    @Before
    public void before() {
        KasiaConnectorImpl connector = new KasiaConnectorImpl();
        connector.setOperationTimeout(20000);
        connector.setKasiaHost(KasiaConnectorImpl.PREPROD_KASIA_HOST);
        client = new OrdreClient();
        client.setConnector(connector);
    }

    @Test
    public void makeInvoice() throws Exception {
        Assert.assertNotSame("Run on preprod..",KasiaConnectorImpl.KASIA_HOST,client.getConnector().getKasiaHost());
        String system="randy-bio";
        String customer="602238134";
        String salesItem="1703001";
        String title="Film title";
        String user="computer";
        InvoiceRequest request=new InvoiceRequest(customer,salesItem,title,user,system);
        InvoiceResponse response = client.makeInvoice(request);
        Assert.assertNotNull("Must return response",response);
        Assert.assertNull("Should have no errors",response.getMessage());
        Assert.assertNotNull("Must give an order",response.getOrderOutput().getUuid());
    }
}
