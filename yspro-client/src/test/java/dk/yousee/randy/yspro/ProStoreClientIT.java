package dk.yousee.randy.yspro;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA. User: aka Date: 07/09/12 Time: 12.41 Test that we
 * can operate pro store
 */
public class ProStoreClientIT {
    private ProStoreClient client;
    private String customer, customer2;

    @Before
    public void setUp() throws Exception {
        ProStoreConnectorImpl connector = new ProStoreConnectorImpl();
        connector.setYsProHost("http://ysprodev.yousee.dk");
        connector.setSystemLogin(connector.getSystemLogin());
        connector.setSystemPassword("We4rAndy");
        connector.setOperationTimeout(20000);
        connector.setAlternativeProxyHost("sltarray02.tdk.dk");
        connector.setAlternativeProxyPort("8080");
        client = new ProStoreClient();
        client.setConnector(connector);
        customer = "607777777";
        customer2 = "617670043";
    }

    @Test
    public void fetchHandle() throws Exception {
        Assert.assertEquals("System login is randy", "RANDY", client.getConnector().getSystemLogin());
        String handleId = client.fetchHandle(0);
        client.setHandleId(handleId);
        Assert.assertNotNull("Handle must be returned", handleId);
    }
//    @Test
//    public void findCustomerProduct() throws Exception {
//        String json6 = client.findCustomerProduct(customer);
//        Assert.assertNotNull("Handle must be returned", json6);
//    }
    private static final String ottProduct = "crudCycleProduct";

    //todo test non existing customer
    @Test
    public void sekvens() throws Exception {
        T1_clean();
        T2_createCustomerProduct();
        T3_updateCustomerProduct();
        T4_crudCycle();
    }

//    @Test
    public void T1_clean() throws Exception {
        ProStoreResponse response = client.removeEngagement(customer, ProStoreDef.YOU_BIO_PRODUCT);

        Assert.assertNotNull("Response must be returned", response);
        Assert.assertEquals("Response must be returned with no error", 0, response.getStatus().intValue());
    }

//    @Test
    public void T2_createCustomerProduct() throws Exception {

        Date now = new Date();
// Create a Store product
        WriteList wl2 = new WriteList(customer, now);
        JsonObject job = new JsonObject();
        job.addProperty(StoreProduct.PRODUCT_ID_YSPRO, ProStoreDef.YOU_BIO_PRODUCT.toString());
        job.addProperty(ProStoreDef.OTT_PRODUCT_KEY_YSPRO, ottProduct);
        job.addProperty(ProStoreDef.BUSINESS_POSITION_KEY_YSPRO, "crm_key_create");
        job.addProperty(ProStoreDef.SERVICE_ITEM_KEY_YSPRO, "stalone_create");
        job.addProperty(ProStoreDef.DESCRIPTION_KEY_YSPRO, "one");
        StoreProduct sp3 = new StoreProduct(job);
        wl2.add(WriteList.Action.add, sp3);
// fire create ....
        String jsonWrite2 = wl2.printJson().toString();
//        URL url2 = client.generateUpdateUrl(customer, jsonWrite2);
        ProStoreResponse response = client.assignProduct(customer, jsonWrite2);
        Assert.assertNotNull("Response must be returned", response);
        Assert.assertEquals("Response must be returned with no error", 0, response.getStatus().intValue());
// Read all OTT products again !!! *** now there must be one fish in the net ***
        ProStoreResponse json3 = client.findEngagement(customer);
        List<StoreProduct> sp4 = json3.filterOttProductWithSignal(ottProduct);
        Assert.assertFalse("After inserting it cannot be empty", sp4.isEmpty());
        Assert.assertEquals("We expect just one", 1, sp4.size());
        StoreProduct product4 = sp4.get(0);
        Assert.assertEquals("Business position must match", "crm_key_create", product4.get(ProStoreDef.BUSINESS_POSITION_KEY_YSPRO));
        Assert.assertEquals("Ott product must match", ottProduct, product4.get(ProStoreDef.OTT_PRODUCT_KEY_YSPRO));
        Assert.assertEquals("Service item must match", "stalone_create", product4.get(ProStoreDef.SERVICE_ITEM_KEY_YSPRO));
        Assert.assertEquals("Description must match", "one", product4.get(ProStoreDef.DESCRIPTION_KEY_YSPRO));
    }

//    @Test
    public void T3_updateCustomerProduct() throws Exception {

        Date now = new Date();
        ProStoreResponse json3 = client.findEngagement(customer);
        List<StoreProduct> sp4 = json3.filterOttProductWithSignal(ottProduct);
        Assert.assertFalse("After inserting it cannot be empty", sp4.isEmpty());
        Assert.assertEquals("We expect just one", 1, sp4.size());

// Update it
        WriteList wl4 = new WriteList(customer, now);
        for (StoreProduct one : sp4) {
            one.set(ProStoreDef.BUSINESS_POSITION_KEY_YSPRO, "crm_key_update");
            one.set(ProStoreDef.SERVICE_ITEM_KEY_YSPRO, "stalone_update");
            one.set(ProStoreDef.DESCRIPTION_KEY_YSPRO, "two");
            wl4.add(WriteList.Action.update, one);
        }
        String jsonWrite4 = wl4.printJson().toString();
//        URL url4 = client.generateUpdateUrl(customer, jsonWrite4);
        ProStoreResponse response4 = client.assignProduct(customer, jsonWrite4);
        Assert.assertNotNull("Response must be returned", response4);
        Assert.assertEquals("Response must be returned with no error", 0, response4.getStatus().intValue());
// after update it, there must be a product
        ProStoreResponse json5 = client.findEngagement(customer);
        Assert.assertNotNull("Handle must be returned", json5);
        List<StoreProduct> sp5 = json5.filterOttProduct(ottProduct);
        Assert.assertEquals("After updating expects one row", 1, sp5.size());
        StoreProduct product6 = sp4.get(0);
        Assert.assertEquals("Business position must match", "crm_key_update", product6.get(ProStoreDef.BUSINESS_POSITION_KEY_YSPRO));
        Assert.assertEquals("Ott product must match", ottProduct, product6.get(ProStoreDef.OTT_PRODUCT_KEY_YSPRO));
        Assert.assertEquals("Service item must match", "stalone_update", product6.get(ProStoreDef.SERVICE_ITEM_KEY_YSPRO));
        Assert.assertEquals("Description must match", "two", product6.get(ProStoreDef.DESCRIPTION_KEY_YSPRO));
    }

//    @Test
    public void T4_crudCycle() throws Exception {

        Date now = new Date();
// Read all OTT products
        ProStoreResponse json0 = client.findOttEngagement(customer);
        Assert.assertNotNull("Handle must be returned", json0);

// Is ottProduct="crudCycleProduct" active on store
        List<StoreProduct> sp0 = json0.filterOttProduct(ottProduct);
        if (!sp0.isEmpty()) {
// yes, get the thing ....
            ProStoreResponse json1 = client.findEngagement(customer);
            List<StoreProduct> sp1 = json1.filterOttProductWithSignal(ottProduct);
            if (!sp1.isEmpty()) {
                WriteList wl = new WriteList(customer, now);
                for (StoreProduct one : sp1) {
                    wl.add(WriteList.Action.delete, one);
                }
// fire deletes ....
                String jsonWrite = wl.printJson().toString();
//                URL url = client.generateUpdateUrl(customer, jsonWrite);
                ProStoreResponse response = client.assignProduct(customer, jsonWrite);
                Assert.assertNotNull("Response must be returned", response);
                Assert.assertEquals("Response must be returned with no error", 0, response.getStatus().intValue());
            }
        }

// Read all OTT products again !!! *** now the list must be empty ***
        ProStoreResponse json2 = client.findOttEngagement(customer);
        List<StoreProduct> sp2 = json2.filterOttProduct(ottProduct);
        Assert.assertTrue("After deleting there must be no more", sp2.isEmpty());

// Create a Store product
        WriteList wl2 = new WriteList(customer, now);
        JsonObject job = new JsonObject();
        job.addProperty(StoreProduct.PRODUCT_ID_YSPRO, ProStoreDef.YOU_BIO_PRODUCT.toString());
        job.addProperty(ProStoreDef.OTT_PRODUCT_KEY_YSPRO, ottProduct);
//        job.addProperty(StoreProduct.OTT_PRODUCT, ottProduct);
        job.addProperty(ProStoreDef.BUSINESS_POSITION_KEY_YSPRO, "crm_key");
        job.addProperty(ProStoreDef.SERVICE_ITEM_KEY_YSPRO, "stalone123");
        StoreProduct sp3 = new StoreProduct(job);
        wl2.add(WriteList.Action.add, sp3);
// fire create ....
        String jsonWrite2 = wl2.printJson().toString();
//        URL url2 = client.generateUpdateUrl(customer, jsonWrite2);
        ProStoreResponse response = client.assignProduct(customer, jsonWrite2);
        Assert.assertNotNull("Response must be returned", response);
        Assert.assertEquals("Response must be returned with no error", 0, response.getStatus().intValue());

// Read all OTT products again !!! *** now there must be one fish in the net ***
        ProStoreResponse json3 = client.findEngagement(customer);
        List<StoreProduct> sp4 = json3.filterOttProductWithSignal(ottProduct);
        Assert.assertFalse("After inserting it cannot be empty", sp4.isEmpty());

// Delete it again
        WriteList wl4 = new WriteList(customer, now);
        for (StoreProduct one : sp4) {
            wl4.add(WriteList.Action.delete, one);
        }
        String jsonWrite4 = wl4.printJson().toString();
//        URL url4 = client.generateUpdateUrl(customer, jsonWrite4);
        response = client.assignProduct(customer, jsonWrite4);
        Assert.assertNotNull("Response must be returned", response);
        Assert.assertEquals("Response must be returned with no error", 0, response.getStatus().intValue());
// after deleted it, there must be no items
        ProStoreResponse json5 = client.findOttEngagement(customer);
        Assert.assertNotNull("Handle must be returned", json5);
        List<StoreProduct> sp5 = json5.filterOttProduct(ottProduct);
        Assert.assertTrue("After deleting it must be gone", sp5.isEmpty());
    }

    public void t1_stb_clean() throws Exception {
        ProStoreResponse response = client.removeEngagement(customer, ProStoreDef.NET_GEM_STB_PRODUCT);
        //client.removeEngagement(customer2, ProStoreDef.NET_GEM_STB_PRODUCT);

        Assert.assertNotNull("Response must be returned", response);
        Assert.assertEquals("Response must be returned with no error", 0, response.getStatus().intValue());
    }

    //@Test
    public void t2_stb_create_on_customer() throws Exception {
        Date now = new Date();

        JsonObject product = new JsonObject();
        product.addProperty(StoreProduct.PRODUCT_ID_YSPRO, ProStoreDef.NET_GEM_STB_PRODUCT.toString());
        product.addProperty(ProStoreDef.OTT_DEVICE_MAC_YSPRO, "12:34:56:78:90:AB");


        StoreProduct sp = new StoreProduct(product);
        WriteList wl = new WriteList(customer, now);

        wl.add(WriteList.Action.add, sp);

        ProStoreResponse response = client.assignProduct(customer, new Gson().toJson(wl.printJson()));
        Assert.assertNotNull("Response must be returned", response);
        Assert.assertEquals("Response must be returned with no error", 0, response.getStatus().intValue());
    }

    public void t3_stb_case1() throws Exception {
        //goal to add 6900 product to customer2, and remove 6900 from customer1 and any other customer having the same device
        //Fetch all customers with mac 12:34:56:78:90:AB
        Date now = new Date();
        final String mac = "12:34:56:78:90:AB";

        ProStoreResponse rs1 = client.findCustomersFromOTTmacStb(mac);
        Assert.assertNotNull("Response must be returned", rs1);
    





        JsonObject product = new JsonObject();
        product.addProperty(StoreProduct.PRODUCT_ID_YSPRO, ProStoreDef.NET_GEM_STB_PRODUCT.toString());
        product.addProperty(ProStoreDef.OTT_DEVICE_MAC_YSPRO, mac);
    }
}
