package dk.yousee.randy.voucher;

import dk.yousee.randy.base.HttpPool;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.logging.Logger;

/**
 * User: aka Date: 06/11/12 Time: 08.56 Test that voucher client works
 */
public class VoucherClientIT {
    private final static Logger logger = Logger.getLogger(VoucherClientIT.class.getName());
    private VoucherClient client = null;

    @Before
    public void setUp() {
        logger.info("setup()");
        HttpPool hp = new HttpPool();
        hp.initPool();
        client = new VoucherClient();
        client.setVoucherHost(VoucherClient.PREPROD_VOUCHER_HOST);
        client.setHttpPool(hp);

    }

    @Test
    public void parseResponse_fail() throws Exception {
        String response =
                "<S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\">\n"
                + "   <S:Body>\n"
                + "      <ns2:consumeTicketResponse xmlns:ns2=\"http://voucher.smarttv.dk/\">\n"
                + "         <return>\n"
                + "            <code>234</code>\n"
                + "            <correlator>corre</correlator>\n"
                + "            <description>VOUCHER_NUMBER_TO_SHORT</description>\n"
                + "            <session_id>516</session_id>\n"
                + "         </return>\n"
                + "      </ns2:consumeTicketResponse>\n"
                + "   </S:Body>\n"
                + "</S:Envelope>";

        VoucherResponse vr = client.parseResponse(response);
        Assert.assertNotNull(vr);
        Assert.assertEquals(response, vr.getXml());
        Assert.assertEquals(234, vr.getCode());
        Assert.assertEquals("corre", vr.getClientReference());
        Assert.assertEquals("VOUCHER_NUMBER_TO_SHORT", vr.getDescription());
        Assert.assertEquals("516", vr.getSession_id());
    }

    @Test
    public void parseResponse_ok() throws Exception {
        String response =
                "<?xml version='1.0' encoding='UTF-8'?>\n"
                + "<S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\">\n"
                + "<S:Body><ns2:consumeTicketResponse xmlns:ns2=\"http://voucher.smarttv.dk/\">\n"
                + "<return>\n"
                + "<code>0</code>\n"
                + "<correlator>record1</correlator>\n"
                + "<description>OK</description>\n"
                + "<session_id>458</session_id>\n"
                + "</return>\n"
                + "</ns2:consumeTicketResponse>\n"
                + "</S:Body>\n"
                + "</S:Envelope>";

        VoucherResponse vr = client.parseResponse(response);
        Assert.assertNotNull(vr);
        Assert.assertEquals(response, vr.getXml());
        Assert.assertEquals(0, vr.getCode());
        Assert.assertEquals("record1", vr.getClientReference());
        Assert.assertEquals("OK", vr.getDescription());
        Assert.assertEquals("458", vr.getSession_id());
    }

    @Test
    public void parseResponse_used() throws Exception {
        String response =
                "<?xml version='1.0' encoding='UTF-8'?>\n"
                + "<S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\">\n"
                + "<S:Body><ns2:consumeTicketResponse xmlns:ns2=\"http://voucher.smarttv.dk/\">\n"
                + "<return>\n"
                + "<code>231</code>\n"
                + "<correlator>record1</correlator>\n"
                + "<description>VOUCHER_ALLREADY_USED</description>\n"
                + "<session_id>246</session_id>\n"
                + "</return></ns2:consumeTicketResponse>\n"
                + "</S:Body>\n"
                + "</S:Envelope>";
        VoucherResponse vr = client.parseResponse(response);
        Assert.assertNotNull(vr);
        Assert.assertEquals(response, vr.getXml());
        Assert.assertEquals(231, vr.getCode());
        Assert.assertEquals("record1", vr.getClientReference());
        Assert.assertEquals("VOUCHER_ALLREADY_USED", vr.getDescription());
        Assert.assertEquals("246", vr.getSession_id());
        Assert.assertNotNull(vr.printJson());
    }

    @Ignore
    @Test
    public void testConsume() {
        Assert.assertNotNull(VoucherClient.PREPROD_VOUCHER_HOST);
        Assert.assertNotNull(client);

        String customer = "618204167";
        String asset = "22145";
        String clientReference = "record1";
        String drm_id = "1";
        String provider = "0900"; //{"voucher":"123455678544","provider":"0900","asset":"22145"}
        Voucher voucher = new Voucher("505468075191");
        Assert.assertNull("voucher must be valid", voucher.verify());
        VoucherRequest request = new VoucherRequest(customer, asset, clientReference, drm_id, provider, voucher);
        VoucherResponse vr = client.consume(request);
        logger.info("response: XML " + vr.xml);
        logger.info("response: value " + vr.toString());
        Assert.assertEquals("Client reference must be returned", clientReference, vr.getClientReference());
        Assert.assertNull("Communication error must be empty", vr.getError());
    }

    @Test
    public void testConsume_used() {
        Assert.assertNotNull(VoucherClient.PREPROD_VOUCHER_HOST);
        Assert.assertNotNull(client);

        String customer = "618204167";
        String asset = "22145";
        String clientReference = "record1";
        String drm_id = "1";
        String provider = "0900"; //{"voucher":"123455678544","provider":"0900","asset":"22145"}
        Voucher voucher = new Voucher("505468075191");
        Assert.assertNull("voucher must be valid", voucher.verify());
        VoucherRequest request = new VoucherRequest(customer, asset, clientReference, drm_id, provider, voucher);
        VoucherResponse vr = client.consume(request);
        logger.info("response: XML " + vr.xml);
        logger.info("response: value " + vr.toString());
        Assert.assertEquals("Client reference must be returned", clientReference, vr.getClientReference());
    }
}
