package dk.yousee.randy.voucher;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.logging.Logger;

/**
 * User: aka
 * Date: 06/11/12
 * Time: 08.56
 * Test that voucher client works
 */
public class VoucherClientIT {

    private final static Logger logger = Logger.getLogger(VoucherClientIT.class.getName());

    private VoucherClient client=null;

    @Before
    public void setUp() {
        logger.info("setup()");
        VoucherConnectorImpl connector = new VoucherConnectorImpl();
        connector.setVoucherHost(VoucherConnectorImpl.VOUCHER_HOST);
//        connector.setVoucherHost(VoucherConnectorImpl.PREPROD_VOUCHER_HOST);
        client = new VoucherClient();
        client.setConnector(connector);

    }

    @Test
    public void readWsdl() throws Exception {
        logger.info("URL: "+client.generateWsdlUrl());
        String wsdl=client.readWsdl();
        Assert.assertNotNull(wsdl);
        Assert.assertTrue(wsdl.contains("consumeTicket"));

    }
    @Test
    public void testConsume() {
        Assert.assertNotNull(VoucherConnectorImpl.PREPROD_VOUCHER_HOST);
        Assert.assertNotNull(client);
    }

}
