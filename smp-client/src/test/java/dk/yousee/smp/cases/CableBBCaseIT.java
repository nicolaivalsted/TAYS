package dk.yousee.smp.cases;

import dk.yousee.smp.casemodel.SubscriberModel;
import dk.yousee.smp.casemodel.vo.ModemId;
import dk.yousee.smp.casemodel.vo.cbp.SuspendHelper;
import dk.yousee.smp.casemodel.vo.cbp.SuspendStatus;
import dk.yousee.smp.functions.OrderServiceImpl;
import dk.yousee.smp.order.model.Acct;
import dk.yousee.smp.order.model.BusinessException;
import dk.yousee.smp.smpclient.SmpConnectorImpl;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Created by IntelliJ IDEA.
 * User: aka
 * Date: Oct 25, 2010
 * Time: 3:10:52 PM
 * Integration tests for cable broadband
 */
@Ignore
public class CableBBCaseIT {
    private static final Logger logger = Logger.getLogger(CableBBCaseIT.class);

    private CableBBCase test;
    private SubscriberModel model;
    private Acct acct;
    private OrderServiceImpl service = null;

    @Before
    public void setup() {
        acct=new Acct("614095141");
//        acct= new Acct("600622411");

//    <bean id="orderService" class="dk.yousee.smp.functions.OrderServiceImpl">
//        <property name="sigmaServiceUrlProperty" value="${dk.yousee.provisioning.config.SigmaServiceUrlProperty}"/>
//        <property name="proxyHost" value="${dk.yousee.provisioning.config.ProxyHost}"/>
//        <property name="proxyPort" value="${dk.yousee.provisioning.config.ProxyPort}"/>
//        <property name="username" value="${dk.yousee.provisioning.config.Username}"/>
//        <property name="password" value="${dk.yousee.provisioning.config.Password}"/>
//    </bean>
        SmpConnectorImpl connector=new SmpConnectorImpl();
        String hostName; int port;
        hostName = "194.239.10.197"; port = 41203; //QA
//        hostName="194.239.10.213"; port=26500; //UDV
//        hostName = "localhost"; port = 8010; //simulator 1
        connector.setUrl(String.format("http://%s:%s/SmpXmlOrderApi/xmlorder", hostName, port));
        connector.setUsername("samp.csra1");
        connector.setPassword("pwcsra1");
//        service.setProxyHost("localhost"); service.setProxyPort("4444");  // used by Anders
//        connector.setProxyHost("sltarray02.tdk.dk"); connector.setProxyPort("8080"); // used by build server
        service = new OrderServiceImpl();
        service.setConnector(connector);
        logger.debug("service allocated");
    }


    @Ignore
    @Test
    public void createBB() {
        test=new CableBBCase(acct,service);
        model=test.getModel();
        Assert.assertTrue("customer must exist",model.customerExists());
        Assert.assertTrue("may have no cable BB data",model.find().CableBBService().isEmpty());

        CableBBCase.AbonData data=new CableBBCase.AbonData();
        data.setPosition("1");
        data.setRateCodes("HSD01");
        data.setEmailServerUnblockProductCode("ABCD");
//        data.setEmailServerUnblockProductCode(null);

        data.setStaticIpProductCode("silver");
//        data.setStaticIpProductCode(null);

        data.setWifiServiceProductCode("2948203984");
//        data.setWifiServiceProductCode(null);

        data.setAddnCPEProductCode("1234");
        int orderId= 0;
        try {
            test.createBB(new ModemId("104300991"),data);
            orderId = test.send();
        } catch (BusinessException e) {
            Assert.fail("should not come here"+e.getMessage());
        }
        Assert.assertTrue(orderId!=0);
    }

    @Test
    public void createBB_no_std_cpe() {
        test=new CableBBCase(acct,service);
        model=test.getModel();
        Assert.assertTrue("customer must exist",model.customerExists());
//        Assert.assertTrue("may have no cable BB data",model.find().CableBBService().isEmpty());

        CableBBCase.AbonData data=new CableBBCase.AbonData();
        data.setRateCodes("1301125");
        data.setUsingStdCpe(false);
        int orderId= 0;
        try {
            test.createBB(new ModemId("106046111"),data);
            orderId = test.send();
        } catch (BusinessException e) {
            Assert.fail("should not come here"+e.getMessage());
        }
        Assert.assertTrue(orderId!=0);
    }

    @Ignore
    @Test
    public void updateBB() {
        test=new CableBBCase(acct,service);
        model=test.getModel();
        Assert.assertTrue("customer must exist",model.customerExists());
        Assert.assertTrue("may have no cable BB data",model.find().CableBBService().isEmpty());

        CableBBCase.AbonData data=new CableBBCase.AbonData();
        data.setRateCodes("1301125");
        data.setEmailServerUnblockProductCode("ABCD");
//        data.setEmailServerUnblockProductCode(null);

        data.setStaticIpProductCode("silver");
//        data.setStaticIpProductCode(null);

        data.setWifiServiceProductCode("2948203984");
//        data.setWifiServiceProductCode(null);

        data.setModemActivationCode("1234");

        int orderId= 0;
        try {
            test.updateBB(new ModemId("104300991"),data);
            orderId = test.send();
        } catch (BusinessException e) {
            Assert.fail("should not come here"+e.getMessage());
        }
        Assert.assertTrue(orderId!=0);
    }

    @Ignore
    @Test
    public void updateBB_modemActivationCode() {
        test=new CableBBCase(acct,service);
        model=test.getModel();
        Assert.assertTrue("customer must exist",model.customerExists());
        Assert.assertFalse("cable BB MUST exist",model.find().CableBBService().isEmpty());

        CableBBCase.AbonData data=new CableBBCase.AbonData();
        data.setRateCodes("1301125");
        data.setModemActivationCode("1234");

        int orderId= 0;
        try {
            test.updateBB(new ModemId("357912345"),data);
            orderId = test.send();
        } catch (BusinessException e) {
            Assert.fail("should not come here"+e.getMessage());
        }
        Assert.assertTrue(orderId!=0);
    }

    /**
     * https://yousee.jira.com/wiki/display/MARKEDFEM/integration-smp shows VRF to use
     */
    @Ignore
    @Test
    public void updateBB_vrf() {
        test=new CableBBCase(acct,service);
        model=test.getModel();
        Assert.assertTrue("customer must exist",model.customerExists());
        Assert.assertFalse("cable BB MUST exist",model.find().CableBBService().isEmpty());

        CableBBCase.AbonData data=new CableBBCase.AbonData();
        data.setVrf("503");

        int orderId= 0;
        try {
            test.updateBB(new ModemId("357912345"),data);
            orderId = test.send();
        } catch (BusinessException e) {
            Assert.fail("should not come here"+e.getMessage());
        }
        Assert.assertTrue(orderId!=0);
    }

    @Ignore
    @Test
    public void updateBB_activationReference() {
        test=new CableBBCase(acct,service);
        model=test.getModel();
        Assert.assertTrue("customer must exist",model.customerExists());
        Assert.assertFalse("cable BB MUST exist",model.find().CableBBService().isEmpty());

        CableBBCase.AbonData data=new CableBBCase.AbonData();
        data.setActivationReference("city-bb");

        int orderId= 0;
        try {
            test.updateBB(new ModemId("357912345"),data);
            orderId = test.send();
        } catch (BusinessException e) {
            Assert.fail("should not come here"+e.getMessage());
        }
        Assert.assertTrue(orderId!=0);
    }

    @Ignore
    @Test
    public void deleteBB() throws Exception {
        test=new CableBBCase(acct,service);
        model=test.getModel();
        Assert.assertTrue("customer must exist",model.customerExists());

        ModemId modemId=test.firstModem();
        test.deleteBB(modemId);
        int orderId=test.send();
        Assert.assertTrue(orderId!=0);
    }

    @Ignore
    @Test
    public void combineBB() throws Exception {
        test=new CableBBCase(acct,service);
        model=test.getModel();
        Assert.assertTrue("customer must exist",model.customerExists());

        ModemId modemId=test.firstModem();
        test.deleteBB(modemId);

        CableBBCase.AbonData data=new CableBBCase.AbonData();
        data.setRateCodes("HSD01");
        data.setEmailServerUnblockProductCode("ABCD");
//        data.setEmailServerUnblockProductCode(null);

        data.setStaticIpProductCode("silver");
//        data.setStaticIpProductCode(null);

        data.setWifiServiceProductCode("2948203984");
//        data.setWifiServiceProductCode(null);
        test.createBB(new ModemId("357912345"),data);

        int orderId=test.send();
        Assert.assertTrue(orderId!=0);
    }

    @Test
    public void construct() throws Exception {
        SubscriberCase customerCase=new SubscriberCase(service,acct);

        test=new CableBBCase(customerCase);
        Assert.assertNotNull(test);
    }

    @Ignore
    @Test
    public void deleteSMPWiFi() throws Exception {
        test=new CableBBCase(acct,service);
        test.deleteSMPWiFi(new ModemId("101824099"));
        test.send();
    }

    @Ignore
    @Test
    public void addSMPWiFi() throws Exception {
        test=new CableBBCase(acct,service);
        test.addSMPWiFi(new ModemId("101824099"),"ABCD","9","test","test");
        test.send();
    }

    @Ignore
    @Test
    public void combineSMPWiFi() throws Exception {
        test=new CableBBCase(acct,service);
        test.deleteSMPWiFi(new ModemId("100000010"));
        test.addSMPWiFi(new ModemId("100000010"),"ABCD","test2","test2","test2");
        test.send();
    }

    @Ignore
    @Test
    public void updateSMPWiFi() throws Exception {
        test=new CableBBCase(acct,service);
        test.updateSMPWiFi(new ModemId("100000010"),"SSID","pwd","1");
        Assert.assertNotNull(test.getModel());
    }

    @Ignore
    @Test
    public void suspendAbuse() throws Exception {
        test=new CableBBCase(acct,service);
        model=test.getModel();
        Assert.assertTrue("customer must exist",model.customerExists());

        ModemId modemId=test.firstModem();
        test.suspendAbuse(modemId, SuspendHelper.SuspendReasonAbuse.ABUSE);
        Assert.assertNotNull(test.getModel());
    }

    @Ignore
    @Test
    public void suspendFlow() throws Exception {
        test=new CableBBCase(new Acct("617020978"),service);
        model=test.getModel();
        Assert.assertTrue("customer must exist",model.customerExists());

        ModemId modemId=test.firstModem();
// Suspend billing
        SuspendStatus status=test.suspendBilling(modemId, SuspendHelper.SuspendReasonBilling.REGNING);
        Assert.assertNotNull(status);
        Integer order=test.send();
        Assert.assertNotNull(order);

        test=new CableBBCase(new Acct("617020978"),service);
        status=test.getSuspendStatus(modemId);
        Assert.assertNotNull(status);
// Suspend abuse
        status=test.suspendAbuse(modemId, SuspendHelper.SuspendReasonAbuse.ABUSE);
        Assert.assertNotNull(status);
        order=test.send();
        Assert.assertNotNull(order);

// resume abuse
        test=new CableBBCase(new Acct("617020978"),service);
        status=test.resumeAbuse(modemId);
        Assert.assertNotNull(status);
        order=test.send();
        Assert.assertNotNull(order);

// resume billing
        test=new CableBBCase(new Acct("617020978"),service);
        status=test.resumeBilling(modemId);
        Assert.assertNotNull(status);
        order=test.send();
        Assert.assertNotNull(order);
    }

    @Ignore
    @Test
    public void resumeAbuse() throws Exception {
        test=new CableBBCase(acct,service);
        model=test.getModel();
        Assert.assertTrue("customer must exist",model.customerExists());

        ModemId modemId=test.firstModem();
        test.resumeAbuse(modemId);
        Assert.assertNotNull(test.getModel());
    }

    @Ignore
    @Test
    public void resumeBilling() throws Exception {
        test=new CableBBCase(new Acct("617020978"),service);
        ModemId modemId=test.firstModem();
        SuspendStatus status=test.resumeBilling(modemId);
        Assert.assertNotNull(status);
        Integer order=test.send();
        Assert.assertNotNull(order);
    }

    @Test
    public void getSuspendStatus() throws Exception {
        test=new CableBBCase(acct,service);
        model=test.getModel();
        Assert.assertTrue("customer must exist",model.customerExists());

        ModemId modemId=test.firstModem();
        SuspendStatus status = test.getSuspendStatus(modemId);
        Assert.assertNotNull(status);
    }
}
