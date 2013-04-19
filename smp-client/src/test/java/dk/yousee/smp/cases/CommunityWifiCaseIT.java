package dk.yousee.smp.cases;

import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import dk.yousee.smp.casemodel.SubscriberModel;
import dk.yousee.smp.casemodel.vo.BusinessPosition;
import dk.yousee.smp.casemodel.vo.cwifi.CommunityWifiService;
import dk.yousee.smp.casemodel.vo.helpers.BasicUnit;
import dk.yousee.smp.functions.OrderServiceImpl;
import dk.yousee.smp.order.model.Acct;
import dk.yousee.smp.order.model.Order;
import dk.yousee.smp.smpclient.SmpConnectorImpl;

/**
 * Created by IntelliJ IDEA.
 * User: aka
 * Date: Oct 25, 2010
 * Time: 3:10:52 PM
 * Integration tests for Forenings mail
 */
@Ignore
public class CommunityWifiCaseIT {
    private static final Logger logger = Logger.getLogger(CommunityWifiCaseIT.class);

    private CommunityWifiCase test;
    private SubscriberModel model;
    private Acct acct;
    private OrderServiceImpl service = null;

    @Before
    public void setup() {
//        acct=new Acct("609562368"); //QA
//        acct=new Acct("200000135"); //UDV
        acct=new Acct("608252633"); //PROD Chr fuglsang

//    <bean id="orderService" class="dk.yousee.smp.functions.OrderServiceImpl">
//        <property name="sigmaServiceUrlProperty" value="${dk.yousee.provisioning.config.SigmaServiceUrlProperty}"/>
//        <property name="proxyHost" value="${dk.yousee.provisioning.config.ProxyHost}"/>
//        <property name="proxyPort" value="${dk.yousee.provisioning.config.ProxyPort}"/>
//        <property name="username" value="${dk.yousee.provisioning.config.Username}"/>
//        <property name="password" value="${dk.yousee.provisioning.config.Password}"/>
//    </bean>

        SmpConnectorImpl connector=new SmpConnectorImpl();
        connector.setSmpHost(SmpConnectorImpl.T_NET_QA_SMP_HOST);
        connector.setSmpHost(SmpConnectorImpl.T_NET_SMP_HOST);
        connector.setUsername("samp.csra1");
        connector.setPassword("pwcsra1");
//        connector.setOperationTimeout(5000);
//        service.setProxyHost("localhost"); service.setProxyPort("4444");  // used by Anders
//        connector.setProxyHost("sltarray02.tdk.dk"); connector.setProxyPort("8080"); // used by build server
        service = new OrderServiceImpl();
        service.setConnector(connector);
        logger.debug("service allocated");
    }


    @Test
    public void step_00_CommunityWifiCase_made_from_subscriber() throws Exception {
        SubscriberCase sCase=new SubscriberCase(service,acct);
        boolean ex=sCase.customerExists();
        Assert.assertTrue("customer must exist",ex);
        test=new CommunityWifiCase(sCase,true);
        model=test.getModel();

        List<CommunityWifiService> services = model.find().CommunityWifiService();
        Assert.assertNotNull("Services can never be null",services);
    }

    @Test
    public void step_01_create() throws Exception {
        test=new CommunityWifiCase(service, acct);
        model=test.getModel();
        Assert.assertTrue("customer must exist",model.customerExists());
        List<CommunityWifiService> services = model.find().CommunityWifiService();
        Assert.assertNotNull("Services can never be null",services);
        CommunityWifiCase.CommunityWifiData data = new CommunityWifiCase.CommunityWifiData();
        BusinessPosition position=BusinessPosition.create("33");
        Order order = test.createProvisioning(position, data);
        Assert.assertNotNull("There should be one", order);
        Integer send = test.send();
        Assert.assertNotNull("Expected order number to create",send);
    }

    @Test
    public void step_02_readProvisioning() throws Exception {
        test=new CommunityWifiCase(service, acct);
        model=test.getModel();
        Assert.assertTrue("customer must exist",model.customerExists());
        List<CommunityWifiService> services = model.find().CommunityWifiService();
        Assert.assertNotNull("Services can never be null",services);
        Assert.assertFalse("There must be at least one service", services.isEmpty());
        CommunityWifiService mail=services.get(0);
        BusinessPosition position=mail.getPosition();

        CommunityWifiCase.CommunityWifiData data = test.readProvisioning(position);
        Assert.assertNotNull("Expected to find data",data);
        Assert.assertNotNull("Expected to product",data.getYsproPcode());
    }

    @Test
    public void step_04_readAll() throws Exception {
        test=new CommunityWifiCase(service, acct);
        model=test.getModel();
        Assert.assertTrue("customer must exist",model.customerExists());
        List<BasicUnit> units=model.filterProgress();
        if(units!=null){
            for(BasicUnit unit:units){
                Assert.assertTrue(unit.isInProgress());
                Assert.assertNull(unit.getName()); // unit must be a sub service it got no name..
            }
        }

        List<CommunityWifiCase.CommunityWifiActivationData> list = test.readAll();

        for(CommunityWifiCase.CommunityWifiActivationData one:list){
            Assert.assertNotNull("ForeningsMailActivationData can never be null",one);
            BusinessPosition pos=one.getPosition();
            Assert.assertNotNull("position can never be null",pos);
            String uuid=one.getUuid();
            Assert.assertNotNull("UUID can never be null",uuid);
        }
    }
    @Test
    public void step_05_delete() throws Exception {
        test=new CommunityWifiCase(service, acct);
        model=test.getModel();
        Assert.assertTrue("customer must exist",model.customerExists());
        List<CommunityWifiService> services = model.find().CommunityWifiService();
        for(CommunityWifiService service:services){
            BusinessPosition pos=service.getPosition();
            boolean any=test.delete(pos);
            logger.debug(String.format("Delete %s ,any=%s",pos,any));
        }
        Integer send = test.send();
        Assert.assertNotNull("Expected order to delete",send);
    }
}
