package dk.yousee.randy.servicemap;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

/**
 * User: aka
 * Date: 06/11/12
 * Time: 08.56
 * Test that Service Map client works
 */
public class SmClientIT {

    private final static Logger logger = Logger.getLogger(SmClientIT.class.getName());

    private SmClient client = null;

    @Before
    public void setUp() {
        SmConnectorImpl connector = new SmConnectorImpl();
        connector.setServiceMapHost(SmConnectorImpl.DEV_HOST);
        connector.setServiceMapHost(SmConnectorImpl.T_HOST);
//        connector.setServiceMapHost(SmConnectorImpl.DEV_HOST);
        client = new SmClient();
        client.setConnector(connector);
    }

    @Test
    public void fetchVendors() throws Exception {
        Assert.assertNotSame(SmConnectorImpl.P_HOST, client.getConnector().getServiceMapHost());
        logger.info("URL: " + client.generateVendorUrl());
        Vendors vendors = client.fetchVendors();
        Assert.assertNotNull(vendors);
        Vendor vendor=vendors.filterByIsp("perspektivbredband");
        Assert.assertNotNull("vendor should exist",vendor);
        Assert.assertNotNull(vendor.getId());
        Assert.assertNotNull(vendor.getVendor());
        Assert.assertNotNull(vendor.getActivationReference());

// find yousee
        Vendor yousee = vendors.filterByIsp(null);
        Assert.assertNotNull("yousee should exist",yousee);
    }

    @Test
    public void fetchMails() throws Exception {
        logger.info("URL: " + client.generateForeningsMailUrl());
        MailResponse response = client.fetchForeningsMails();
        Assert.assertNotNull(response);
    }

    @Test
    public void fetchMailsByAnlaeg() throws Exception {
        String anlaeg="4002525";
        logger.info("URL: " + client.generateForeningsMailUrlByAnlaeg(anlaeg));
        MailResponse response = client.fetchForeningsMailsByAnlaeg(anlaeg);
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getReadTime());
        Assert.assertNotNull(response.getInput());
        Assert.assertNull(response.getMessage());
        Assert.assertEquals("one row",1,response.getRows().size());
        MailRow row=response.getRows().get(0);
        Assert.assertEquals(anlaeg,row.getAnlaeg());
        Assert.assertEquals("ystest",row.getProductName());
        Assert.assertEquals("foreningsmail.dk",row.getProductCode());
        Assert.assertEquals("1001",row.getSubPos());
    }

    @Test
    public void fetchItem() throws Exception {
        Set<String> items=new HashSet<String>();
        items.add("1991000");
        items.add("1340391");

        logger.info("URL: " + client.generateItemUrl(items));
        ItemResponse response = client.fetchItem(items);
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getReadTime());
        Assert.assertNotNull(response.getInput());
        Assert.assertNull(response.getMessage());

        Assert.assertEquals("one row",2,response.getRows().size());
        for(ItemRow row: response.getRows()){
            logger.info(row.printJson().toString());
        }
//        Assert.assertEquals(row,row.getAnlaeg());
//        Assert.assertEquals("ystest",row.getProductName());
//        Assert.assertEquals("foreningsmail.dk",row.getProductCode());
//        Assert.assertEquals("1001",row.getSubPos());
    }
}
