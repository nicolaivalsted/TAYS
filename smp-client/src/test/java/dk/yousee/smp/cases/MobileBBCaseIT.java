package dk.yousee.smp.cases;

import dk.yousee.smp.casemodel.SubscriberModel;
import dk.yousee.smp.casemodel.vo.ModemId;
import dk.yousee.smp.functions.OrderServiceImpl;
import dk.yousee.smp.order.model.Acct;
import dk.yousee.smp.order.model.BusinessException;
import dk.yousee.smp.order.model.Response;
import dk.yousee.smp.smpclient.SmpConnectorImpl;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

/**
 * User: aka
 * Date: Oct 25, 2010
 * Time: 3:10:52 PM
 * Integration tests for mobile broadband
 */
@Ignore
public class MobileBBCaseIT {
    private static final Logger logger = Logger.getLogger(SubscriberCaseTest.class);

    private MobileBBCase test;
    private SubscriberModel model;
    private Acct acct;
    String orderUrl = null;
    OrderServiceImpl service;

    @Before
    public void setup() {
        orderUrl = "http://194.239.10.197:41203/bss-adapter2/order.service";
//        proxy = null;
//        orderUrl="http://194.239.10.213:26500/bss-adapter2/order.service";
        orderUrl="http://localhost:7777/order.service";
        acct = new Acct("614884755");
        Response response;
        response = new Response();
        response.setAcct(acct);
        model = new SubscriberModel(response);


        SmpConnectorImpl connector=new SmpConnectorImpl();
        connector.setSmpHost(SmpConnectorImpl.T_NET_QA_SMP_HOST);
        connector.setUsername("samp.csra1");
        connector.setPassword("pwcsra1");
        service = new OrderServiceImpl();
        service.setConnector(connector);

    }

    @Test
    public void debugCreateMoBB() {

        logger.debug("service allocated");
        test = new MobileBBCase(acct, service);
        model = test.getModel();
        MobileBBCase.AbonData data = new MobileBBCase.AbonData();
        data.setMsisdn("29837973");
        data.setVarenummer("1601003");
        data.setIcc("89450100090514005173");
        int orderId = 0;
        try {
            test.createMoBB(new ModemId("105087403"), data);
            orderId = test.send();
        } catch (BusinessException e) {
            Assert.fail("should not come here" + e.getMessage());
        }
        test.getModel().find().SMPMobileBroadbandAttributes(new ModemId("105087403"));
        Assert.assertTrue(orderId != 0);
    }


    @Ignore
    @Test
    public void createMoBB() {
        test = new MobileBBCase(acct, service);
        model = test.getModel();
        Assert.assertTrue("customer must exist", model.customerExists());
        Assert.assertTrue("may have no mobile data", model.find().MobileBBService().isEmpty());

        MobileBBCase.AbonData data = new MobileBBCase.AbonData();
        data.setMsisdn("29836058");
        data.setVarenummer("1601001");
        data.setIcc("89450100080724039617");
        int orderId = 0;
        try {
            test.createMoBB(new ModemId("100507451"), data);
            orderId = test.send();
        } catch (BusinessException e) {
            Assert.fail("should not come here" + e.getMessage());
        }
        test.getModel().find().SMPMobileBroadbandAttributes(new ModemId("100507451"));
        Assert.assertTrue(orderId != 0);
    }

    @Ignore
    @Test
    public void getMobileSubscriberDetails() throws Exception {
        test = new MobileBBCase(acct, service);
        List<MobileBBCase.MobileBroadband> broadbandList = test.getMobileSubscriberDetails();
        Assert.assertEquals(1, broadbandList.size());
        MobileBBCase.MobileBroadband row = broadbandList.get(0);
        Assert.assertEquals(test.firstModem(), row.getModemId());
        Assert.assertEquals("29832340", row.getMsisdn());
        Assert.assertEquals("1601001", row.getVarenummer());
        Assert.assertEquals("89450100080724015443", row.getIcc());
        Assert.assertNotNull(row.getState());
        Assert.assertNotNull(row.getSuspensionTypeId());
    }

    @Ignore
    @Test
    public void updateMoBB() throws Exception {
        test = new MobileBBCase(acct, service);
        model = test.getModel();
        ModemId modemId = test.firstModem();

        MobileBBCase.AbonData data = new MobileBBCase.AbonData();
        data.setMsisdn("29833179");
        data.setVarenummer("1601001");
        data.setIcc("89450100080724020300");
        test.updateMoBB(modemId, data, false);
        int orderId = test.send();
        Assert.assertTrue(orderId != 0);
    }


    @Ignore
    @Test
    public void suspendMoBB() throws Exception {
        test = new MobileBBCase(acct, service);
        model = test.getModel();
        Assert.assertTrue("customer must exist", model.customerExists());

        ModemId modemId = test.firstModem();
//        MobileBBService mobileBBService = test.getModel().find().MobileBBService(modemId);
//        ProvisionStateEnum provisionStateEnumState = mobileBBService.getServicePlanState();
        boolean anything = test.suspendMoBB(modemId);
        Assert.assertTrue("must make order data", anything);
        int orderId = test.send();
        Assert.assertTrue(orderId != 0);
    }

    @Ignore
    @Test
    public void resumeMoBB() throws Exception {
        test = new MobileBBCase(acct, service);
        model = test.getModel();
        Assert.assertTrue("customer must exist", model.customerExists());

        ModemId modemId = test.firstModem();
        boolean anything = test.resumeMoBB(modemId);
        Assert.assertTrue("must make order data", anything);
        int orderId = test.send();
        Assert.assertTrue(orderId != 0);
    }

    @Ignore
    @Test
    public void deleteMoBB() throws Exception {
        test = new MobileBBCase(acct, service);
        model = test.getModel();
        Assert.assertTrue("customer must exist", model.customerExists());

        ModemId modemId = test.firstModem();
        test.deleteMoBB(modemId);
        int orderId = test.send();
        Assert.assertTrue(orderId != 0);
    }

//    @Test
//    public void closeBB() throws Exception {
//        BssAdapterClient bssAdapter;
//        bssAdapter = new BssAdapterClient(orderUrl);
//        logger.debug("BssAdapterClient allocated, url="+orderUrl);
//        OrderService service = bssAdapter.getOrderService();
//        logger.debug("service allocated");
//        test=new MobileBBCase(acct,service);
//        model=test.getModel();
//        test.closeMoBB(new ModemId("101371329"));
//        String orderId=test.send();
//        Assert.assertNotNull(orderId);
//
//    }

    @Test
    public void construct() throws Exception {
        SubscriberCase customerCase = new SubscriberCase(service, acct);
        test = new MobileBBCase(customerCase);
        Assert.assertNotNull(test);
    }

}
