package dk.yousee.randy.kasia;

import com.google.gson.JsonElement;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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
        Assert.assertNotSame("Run on preprod..", KasiaConnectorImpl.KASIA_HOST, client.getConnector().getKasiaHost());
        String system = "randy-bio";
        String customer = "602238134";
        String salesItem = "1703001";
        String title = "Film title";
        String user = "computer";
        InvoiceRequest request = new InvoiceRequest(customer, salesItem, title, user, system);
        InvoiceResponse response = client.makeInvoice(request);
        Assert.assertNotNull("Must return response", response);
        Assert.assertNull("Should have no errors", response.getMessage());
        Assert.assertNotNull("Must give an order", response.getOrderOutput().getUuid());
    }

    private static final String ordreId = "b650255e-45b1-4d2d-8f6f-1bb57e96ed8f";
//    private static final String ordreId = "bf306e71-f571-4544-bc19-062f2a1f9975";//"3442db26-ddad-4c4c-addc-e49f0f32f62c";

    @Test
    public void queryOrdre_existing() throws Exception {
        OrderStateResponse response = client.queryOrdre(ordreId);
        Assert.assertNotNull("Must return response", response);
        JsonElement jsonSource = response.getJsonSource();
        Assert.assertNotNull("Must give a json", jsonSource);
        String message = response.getMessage();
        if (message != null) {
            System.out.println("message=" + message);
            System.out.println("json=" + jsonSource.toString());
        }
        Assert.assertNull("Should have no errors", message);
        Assert.assertNotNull("Must give a status", response.getStatus());
    }

    @Test
    public void queryOrdre_notExists() throws Exception {
        OrderStateResponse response = client.queryOrdre("en-bade-bold");
        Assert.assertNotNull("Must return response", response);
        Assert.assertNotNull("Should have errors", response.getMessage());
        Assert.assertNull("Cannot contain a status", response.getStatus());
    }

    private static final String[] filmRentalItemIds = new String[]{
        "1703000", //YouBio Film 0
        "1703001", //YouBio Film 1
        "1703002", //YouBio Film 2
        "1703003", //YouBio Film 3
        "1703004", //YouBio Film 4
        "1703005"  //YouBio Film 5
    };

    @Test
    public void prices() throws Exception {
        List<String> itemKeys = new ArrayList<String>();
        Collections.addAll(itemKeys, filmRentalItemIds);
        PricesResponse response = client.prices(itemKeys);
        Assert.assertNotNull("Must return response", response);
        JsonElement jsonSource = response.getJsonSource();
        Assert.assertNotNull("Must give a json", jsonSource);
        String message = response.getMessage();
        if (message != null) {
            System.out.println("message=" + message);
            System.out.println("json=" + jsonSource.toString());
        }
        Assert.assertNull("Should have no errors", message);
        Map<String, ItemPrice> items = response.getItems();
        Assert.assertFalse("Must contain data", items.isEmpty());
        Collection list = response.asList();
        Assert.assertNotNull(list);
        Assert.assertNotNull(response.getReadTimeAsString());
        Assert.assertNotNull("Should return a price", response.filterByPrice("9,00"));
        Assert.assertNotNull("Should return list of prices", response.availablePrices());
    }

}
