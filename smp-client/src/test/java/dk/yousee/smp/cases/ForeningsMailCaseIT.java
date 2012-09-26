package dk.yousee.smp.cases;

import dk.yousee.smp.casemodel.SubscriberModel;
import dk.yousee.smp.casemodel.vo.BusinessPosition;
import dk.yousee.smp.casemodel.vo.mail.ForeningsMailService;
import dk.yousee.smp.casemodel.vo.mail.Mail;
import dk.yousee.smp.functions.OrderServiceImpl;
import dk.yousee.smp.order.model.Acct;
import dk.yousee.smp.order.model.Order;
import dk.yousee.smp.smpclient.SmpConnectorImpl;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: aka
 * Date: Oct 25, 2010
 * Time: 3:10:52 PM
 * Integration tests for Forenings mail
 */
@Ignore
public class ForeningsMailCaseIT {
    private static final Logger logger = Logger.getLogger(SubscriberCaseTest.class);

    private ForeningsMailCase test;
    private SubscriberModel model;
    private Acct acct;
    private OrderServiceImpl service = null;

    @Before
    public void setup() {
        acct=new Acct("609562368"); //QA
//        acct=new Acct("200000135"); //UDV
//        acct=new Acct("608252633"); //PROD

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
        connector.setProxyHost("sltarray02.tdk.dk"); connector.setProxyPort("8080"); // used by build server
        service = new OrderServiceImpl();
        service.setConnector(connector);
        logger.debug("service allocated");
    }


    @Test
    public void step_00_ForeningsMailCase_made_from_subscriber() throws Exception {
        SubscriberCase sCase=new SubscriberCase(service,acct);
        boolean ex=sCase.customerExists();
        Assert.assertTrue("customer must exist",ex);
        test=new ForeningsMailCase(sCase,true);
        model=test.getModel();

        List<ForeningsMailService> services = model.find().ForeningsMailService();
        Assert.assertNotNull("Services can never be null",services);
    }

    @Test
    public void step_01_create() throws Exception {
        test=new ForeningsMailCase(service, acct);
        model=test.getModel();
        Assert.assertTrue("customer must exist",model.customerExists());
        List<ForeningsMailService> services = model.find().ForeningsMailService();
        Assert.assertNotNull("Services can never be null",services);
        ForeningsMailCase.ForeningsData data = new ForeningsMailCase.ForeningsData();
        data.setProduct("turbo.dk");
        data.setName("Dette er navnet på turbo.dk");
        BusinessPosition position=BusinessPosition.create("33");
        Order order = test.createProvisioning(position, data);
        Assert.assertNotNull("There should be one", order);
        Integer send = test.send();
        Assert.assertNotNull("Expected order number to create",send);
    }

    @Test
    public void step_02_readProvisioning() throws Exception {
        test=new ForeningsMailCase(service, acct);
        model=test.getModel();
        Assert.assertTrue("customer must exist",model.customerExists());
        List<ForeningsMailService> services = model.find().ForeningsMailService();
        Assert.assertNotNull("Services can never be null",services);
        Assert.assertFalse("There must be at least one service", services.isEmpty());
        ForeningsMailService mail=services.get(0);
        BusinessPosition position=mail.getPosition();

        ForeningsMailCase.ForeningsData data = test.readProvisioning(position);
        Assert.assertNotNull("Expected to find data",data);
        Assert.assertNotNull("Expected to product",data.getProduct());
        String customerId=mail.getMail().customer_id.getValue();
        Assert.assertNotNull("Expected to find a customer",customerId);
    }

    @Test
    public void step_03_activate() throws Exception {
        test=new ForeningsMailCase(service, acct);
        model=test.getModel();
        Assert.assertTrue("customer must exist",model.customerExists());

        List<ForeningsMailCase.ForeningsMailActivationData> list = test.readAll();
        
        for(ForeningsMailCase.ForeningsMailActivationData one:list){
            Assert.assertNotNull("ForeningsMailActivationData can never be null",one);
            BusinessPosition pos=one.getPosition();
            Assert.assertNotNull("position can never be null",pos);
            String product=one.getProduct();
            Assert.assertNotNull("product can never be null",product);
            Order order = test.activate(pos, true);
            Assert.assertNotNull("Order can not be null",order);
            Mail mail=model.find().ForeningsMail(pos);
            Assert.assertNotNull("Mail can not be null",product);
            Assert.assertNotNull("Conversation must be set now", mail.getConversation());
            Assert.assertTrue("Conversation should now be true", mail.getConversation());
            Assert.assertNotNull("Conversation date must be set now", mail.getConversationDate());
        }
        Integer send = test.send();
        Assert.assertNotNull("Expected order from activate",send);
    }

    @Test
    public void step_04_readAll() throws Exception {
        test=new ForeningsMailCase(service, acct);
        model=test.getModel();
        Assert.assertTrue("customer must exist",model.customerExists());

        List<ForeningsMailCase.ForeningsMailActivationData> list = test.readAll();

        for(ForeningsMailCase.ForeningsMailActivationData one:list){
            Assert.assertNotNull("ForeningsMailActivationData can never be null",one);
            BusinessPosition pos=one.getPosition();
            Assert.assertNotNull("position can never be null",pos);
            String product=one.getProduct();
            Assert.assertNotNull("product can never be null",product);
            Mail mail=model.find().ForeningsMail(pos);
            Assert.assertNotNull("Mail can not be null",product);
            Assert.assertNotNull("Conversation must be set now", mail.getConversation());
            Assert.assertEquals(Boolean.TRUE, mail.getConversation());
            Assert.assertNotNull("Conversation date must be set now", mail.getConversationDate());
        }
    }
    @Test
    public void step_05_delete() throws Exception {
        test=new ForeningsMailCase(service, acct);
        model=test.getModel();
        Assert.assertTrue("customer must exist",model.customerExists());
        List<ForeningsMailService> services = model.find().ForeningsMailService();
        for(ForeningsMailService service:services){
            BusinessPosition pos=service.getPosition();
            boolean any=test.delete(pos);
            logger.debug(String.format("Delete %s ,any=%s",pos,any));
        }
        Integer send = test.send();
        Assert.assertNotNull("Expected order to delete",send);
    }
}
