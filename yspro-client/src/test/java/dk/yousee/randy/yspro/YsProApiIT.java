package dk.yousee.randy.yspro;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dk.yousee.randy.base.HttpPool;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;
import org.joda.time.DateTime;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author m27236
 */
public class YsProApiIT {
    private static YsProApi api;
    private static ProStoreConnectorImpl connector;
    private static String customer, customer2;
    private static final String ottProduct = "crudCycleProduct";
    private static HttpPool pool;

    @BeforeClass
    public static void before() {
        pool = new HttpPool();
        pool.initPool();
        connector = new ProStoreConnectorImpl(3000,8000);
        connector.setPool(pool);
        connector.setYsProHost(ProStoreConnectorImpl.TEST_YSPRO_HOST);
        connector.setSystemLogin(connector.getSystemLogin());
        connector.setSystemPassword("We4rAndy");

        api = new YsProApi();
        api.setClient(connector);

        customer = "608252633";
        customer2 = "617670043";
    }

    @AfterClass
    public static void after() {
        pool.shutdown();
    }

//    @Test
    public void testLogin() throws YsProException, UnknownHostException {

        ProStoreResponse res = api.getNewPasswordMethods("Simon kjær", Inet4Address.getByName("10.10.10.10"));

        Assert.assertNotNull(res);
    }

//    @Test
    public void testSetPassword() throws YsProException {
        ProStoreResponse res = api.setPassword("2307030", "Cool=Bro1");
        Assert.assertNotNull(res);
    }

//    @Test
    public void testSetUser() throws YsProException {
        ProStoreResponse res = api.updateUserInfo("2307030", "assSikj2=", "assSikj", "sikj@tdc.dk", "30549420");
        Assert.assertNotNull(res);
    }

//    @Test
    public void testGetmailInfo() throws YsProException {
        final String kpm = "120108085626";

        ProStoreResponse psr = api.findMailGetInfo(kpm);
        Assert.assertNotNull(psr);

        Assert.assertTrue(psr.getStatus() == 0);
    }

//    @Test
    public void testGetNewPasswordMethods() throws YsProException, UnknownHostException {

        ProStoreResponse e = api.getNewPasswordMethods("SimonOfk", InetAddress.getByName("4.4.4.4"));

        Assert.assertNotNull(e);
    }

//    @Test
    public void userInfo() throws Exception {
        String userId = "168851";
        UserInfo userInfo = api.findUserInfo(userId);
        Assert.assertNotNull(userInfo);
        Assert.assertEquals("customer must be", "612148865", userInfo.getCustomer());
        Assert.assertEquals("Expected user as the query user", userId, userInfo.getUserId());
        Assert.assertTrue("This user has a credit card", userInfo.isDibs());

        Assert.assertTrue("status is 0", userInfo.getStatus() == 0);
        Assert.assertEquals("OK message expected", "OK", userInfo.getMessage());
        Assert.assertNotNull("json must exist", userInfo.printJson());
//        Assert.assertNotNull("Input is assumed to be assigned", userInfo.getInput());

    }

//    @Test
    public void userInfo_missing() throws Exception {
        UserInfo userInfo = api.findUserInfo("hundeogkatteerdyr");
        Assert.assertNotNull(userInfo);
        Assert.assertNotSame("status is not 0", 0, userInfo.getStatus());
        Assert.assertNotNull("Expects a message", userInfo.getMessage());
    }

//    @Test
    public void sekvens() throws Exception {
        T1_clean();
        T2_createCustomerProduct();
        T3_updateCustomerProduct();
        T4_crudCycle();
    }

    public void T1_clean() throws Exception {
        Date now = new Date();
        WriteList wl2 = new WriteList(customer, now);
        JsonObject job = new JsonObject();
        job.addProperty(StoreProduct.PRODUCT_ID_YSPRO, ProStoreDef.YOU_BIO_PRODUCT.toString());
        StoreProduct sp3 = new StoreProduct(job);
        wl2.add(WriteList.Action.delete, sp3);
        String jsonWrite2 = wl2.printJson().toString();
        ProStoreResponse response = api.assignProduct(customer, jsonWrite2);

        Assert.assertNotNull("Response must be returned", response);
        Assert.assertEquals("Response must be returned with no error", 0, response.getStatus().intValue());
    }

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
        ProStoreResponse response = api.assignProduct(customer, jsonWrite2);
        Assert.assertNotNull("Response must be returned", response);
        Assert.assertEquals("Response must be returned with no error", 0, response.getStatus().intValue());
// Read all OTT products again !!! *** now there must be one fish in the net ***
        ProStoreResponse json3 = api.findEngagement(customer);
        List<StoreProduct> sp4 = json3.filterOttProductWithSignal(ottProduct);
        Assert.assertFalse("After inserting it cannot be empty", sp4.isEmpty());
        StoreProduct product4 = sp4.get(0);
        Assert.assertEquals("Business position must match", "crm_key_create", product4.get(ProStoreDef.BUSINESS_POSITION_KEY_YSPRO));
        Assert.assertEquals("Ott product must match", ottProduct, product4.get(ProStoreDef.OTT_PRODUCT_KEY_YSPRO));
        Assert.assertEquals("Service item must match", "stalone_create", product4.get(ProStoreDef.SERVICE_ITEM_KEY_YSPRO));
        Assert.assertEquals("Description must match", "one", product4.get(ProStoreDef.DESCRIPTION_KEY_YSPRO));
    }

    public void T3_updateCustomerProduct() throws Exception {

        Date now = new Date();
        ProStoreResponse json3 = api.findEngagement(customer);
        List<StoreProduct> sp4 = json3.filterOttProductWithSignal(ottProduct);
        Assert.assertFalse("After inserting it cannot be empty", sp4.isEmpty());

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
        ProStoreResponse response4 = api.assignProduct(customer, jsonWrite4);
        Assert.assertNotNull("Response must be returned", response4);
        Assert.assertEquals("Response must be returned with no error", 0, response4.getStatus().intValue());
// after update it, there must be a product
        ProStoreResponse json5 = api.findEngagement(customer);
        Assert.assertNotNull("Handle must be returned", json5);
        List<StoreProduct> sp5 = json5.filterOttProduct(ottProduct);
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
        ProStoreResponse json0 = api.findOttEngagement(customer);
        Assert.assertNotNull("Handle must be returned", json0);

// Is ottProduct="crudCycleProduct" active on store
        List<StoreProduct> sp0 = json0.filterOttProduct(ottProduct);
        if (!sp0.isEmpty()) {
// yes, get the thing ....
            ProStoreResponse json1 = api.findEngagement(customer);
            List<StoreProduct> sp1 = json1.filterOttProductWithSignal(ottProduct);
            if (!sp1.isEmpty()) {
                WriteList wl = new WriteList(customer, now);
                for (StoreProduct one : sp1) {
                    wl.add(WriteList.Action.delete, one);
                }
// fire deletes ....
                String jsonWrite = wl.printJson().toString();
//                URL url = client.generateUpdateUrl(customer, jsonWrite);
                ProStoreResponse response = api.assignProduct(customer, jsonWrite);
                Assert.assertNotNull("Response must be returned", response);
                Assert.assertEquals("Response must be returned with no error", 0, response.getStatus().intValue());
            }
        }

// Read all OTT products again !!! *** now the list must be empty ***
        ProStoreResponse json2 = api.findOttEngagement(customer);
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
        ProStoreResponse response = api.assignProduct(customer, jsonWrite2);
        Assert.assertNotNull("Response must be returned", response);
        Assert.assertEquals("Response must be returned with no error", 0, response.getStatus().intValue());

// Read all OTT products again !!! *** now there must be one fish in the net ***
        ProStoreResponse json3 = api.findEngagement(customer);
        List<StoreProduct> sp4 = json3.filterOttProductWithSignal(ottProduct);
        Assert.assertFalse("After inserting it cannot be empty", sp4.isEmpty());

// Delete it again
        WriteList wl4 = new WriteList(customer, now);
        for (StoreProduct one : sp4) {
            wl4.add(WriteList.Action.delete, one);
        }
        String jsonWrite4 = wl4.printJson().toString();
//        URL url4 = client.generateUpdateUrl(customer, jsonWrite4);
        response = api.assignProduct(customer, jsonWrite4);
        Assert.assertNotNull("Response must be returned", response);
        Assert.assertEquals("Response must be returned with no error", 0, response.getStatus().intValue());
// after deleted it, there must be no items
        ProStoreResponse json5 = api.findOttEngagement(customer);
        Assert.assertNotNull("Handle must be returned", json5);
        List<StoreProduct> sp5 = json5.filterOttProduct(ottProduct);
        Assert.assertTrue("After deleting it must be gone", sp5.isEmpty());
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

        ProStoreResponse response = api.assignProduct(customer, new Gson().toJson(wl.printJson()));
        Assert.assertNotNull("Response must be returned", response);
        Assert.assertEquals("Response must be returned with no error", 0, response.getStatus().intValue());
    }

//    @Test
    public void t3_stb_case1() throws Exception {
        //goal to add 6900 product to customer2, and remove 6900 from customer1 and any other customer having the same device
        //Fetch all customers with mac 12:34:56:78:90:AB
        Date now = new Date();
        final String mac = "12:34:56:78:90:AB";

        ProStoreResponse rs1 = api.findCustomersFromOTTmacStb(mac);
        Assert.assertNotNull("Response must be returned", rs1);

        //find all customer having mac but are not customer2
        List<StoreProduct> productsToDelete = rs1.filterCustomerDifferntFrom(customer2);

        for (StoreProduct p : productsToDelete) {
            WriteList wl = new WriteList(p.getCustomer(), now);
            wl.add(WriteList.Action.delete, p);
            api.assignProduct(p.getCustomer(), new Gson().toJson(wl.printJson()));
        }

        ProStoreResponse rs2 = api.findCustomersFromOTTmacStb(mac);
        Assert.assertTrue("There should only be max one active product for mac device", rs2.filterOpen().size() <= 1);

        //if customer2 not in rs2 response then create product for customer2
        if (rs2.getProducts().isEmpty() || !rs2.getProducts().get(0).getCustomer().equals(customer2)) {
            WriteList wl = new WriteList(customer2, now);
            JsonObject product = new JsonObject();
            product.addProperty(StoreProduct.PRODUCT_ID_YSPRO, ProStoreDef.NET_GEM_STB_PRODUCT.toString());
            product.addProperty(ProStoreDef.OTT_DEVICE_MAC_YSPRO, mac);
            StoreProduct sp = new StoreProduct(product);
            wl.add(WriteList.Action.add, sp);

            ProStoreResponse rs3 = api.assignProduct(customer2, wl.printJson().toString());

            Assert.assertNotNull(rs3);
            Assert.assertEquals("Response must be returned with no error", 0, rs3.getStatus().intValue());
        }
    }

//    @Test
    public void login() throws YsProException {
        String session = api.login("SimonOFKtestIT", "");
        Assert.assertNotNull(session);
    }

//    @Test
    public void opretProd() throws YsProException {
        String kunde = "608252633";
        WriteList wl2 = new WriteList(kunde, new Date());
        JsonObject job = new JsonObject();
        job.addProperty(StoreProduct.PRODUCT_ID_YSPRO, ProStoreDef.YOU_BIO_PRODUCT.toString());
        job.addProperty(ProStoreDef.OTT_PRODUCT_KEY_YSPRO, "50serier");
        job.addProperty(ProStoreDef.BUSINESS_POSITION_KEY_YSPRO, "1337");
        job.addProperty(ProStoreDef.SERVICE_ITEM_KEY_YSPRO, "1990050");
        job.addProperty(ProStoreDef.DESCRIPTION_KEY_YSPRO, "50 serier");
        StoreProduct sp3 = new StoreProduct(job);
        wl2.add(WriteList.Action.add, sp3);
// fire create ....
        String jsonWrite2 = wl2.printJson().toString();
//        URL url2 = client.generateUpdateUrl(customer, jsonWrite2);
        ProStoreResponse response = api.assignProduct(kunde, jsonWrite2);

        Assert.assertTrue(response.getStatus() == 0);

        WriteList wl3 = new WriteList(kunde, new Date());
        job = new JsonObject();
        job.addProperty(StoreProduct.PRODUCT_ID_YSPRO, ProStoreDef.YOU_BIO_PRODUCT.toString());
        job.addProperty(ProStoreDef.OTT_PRODUCT_KEY_YSPRO, "1000film");
        job.addProperty(ProStoreDef.BUSINESS_POSITION_KEY_YSPRO, "1337");
        job.addProperty(ProStoreDef.SERVICE_ITEM_KEY_YSPRO, " 1991000");
        job.addProperty(ProStoreDef.DESCRIPTION_KEY_YSPRO, "Tusinde film");
        StoreProduct sp4 = new StoreProduct(job);
        wl3.add(WriteList.Action.add, sp4);
// fire create ....
        jsonWrite2 = wl3.printJson().toString();
//        URL url2 = client.generateUpdateUrl(customer, jsonWrite2);
        response = api.assignProduct(kunde, jsonWrite2);

        Assert.assertTrue(response.getStatus() == 0);

        WriteList wl4 = new WriteList(kunde, new Date());
        job = new JsonObject();
        job.addProperty(StoreProduct.PRODUCT_ID_YSPRO, ProStoreDef.YOU_BIO_PRODUCT.toString());
        job.addProperty(ProStoreDef.OTT_PRODUCT_KEY_YSPRO, "xCmore");
        job.addProperty(ProStoreDef.BUSINESS_POSITION_KEY_YSPRO, "1337");
        job.addProperty(ProStoreDef.SERVICE_ITEM_KEY_YSPRO, "1990008");
        job.addProperty(ProStoreDef.DESCRIPTION_KEY_YSPRO, "Nogle C-more kanaler");
        StoreProduct sp5 = new StoreProduct(job);
        wl4.add(WriteList.Action.add, sp5);
// fire create ....
        jsonWrite2 = wl4.printJson().toString();
//        URL url2 = client.generateUpdateUrl(customer, jsonWrite2);
        response = api.assignProduct(kunde, jsonWrite2);

        Assert.assertTrue(response.getStatus() == 0);

    }

//    @Test
    public void removeOtt() throws YsProException {
        String customer = "612148865";
        List<StoreProduct> sp = api.findEngagementFromProductId(customer, "7000").filterOpen();

        WriteList wl = new WriteList(customer, new Date());
        for (StoreProduct one : sp) {
            wl.add(WriteList.Action.delete, one);
        }
        
         String json = wl.printJson().toString();
        api.assignProduct(customer, json);
    }

//    @Test
    public void testbasicInfo() throws YsProException {
        String session = api.login("assSikj", "ass123");
        Assert.assertNotNull(session);

        String userId = api.findBasicUserInfo(session).getData().get("UserID").getAsString();
        Assert.assertNotNull(userId);
    }
    
    @Test
    public void terminateOnDateTest() throws YsProException {
        
        api.terminateProductOnDate("636f1dbc-c7ad-403f-9b2f-b49a568c5538", new DateTime().plusMonths(1).dayOfMonth().withMaximumValue().toDate());
    }
}
