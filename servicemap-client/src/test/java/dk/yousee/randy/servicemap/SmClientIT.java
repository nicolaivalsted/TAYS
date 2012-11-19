package dk.yousee.randy.servicemap;

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
public class SmClientIT {

    private final static Logger logger = Logger.getLogger(SmClientIT.class.getName());

    private SmClient client = null;

    @Before
    public void setUp() {
        logger.info("setup()");
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
        logger.info("URL: " + client.generateVendorlUrl());
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
}
