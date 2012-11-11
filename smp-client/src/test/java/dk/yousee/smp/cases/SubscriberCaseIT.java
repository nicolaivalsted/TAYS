package dk.yousee.smp.cases;

import dk.yousee.smp.casemodel.SubscriberModel;
import dk.yousee.smp.casemodel.vo.base.SubAddressSpec;
import dk.yousee.smp.casemodel.vo.base.SubContactSpec;
import dk.yousee.smp.casemodel.vo.helpers.BasicUnit;
import dk.yousee.smp.functions.OrderServiceImpl;
import dk.yousee.smp.order.model.Acct;
import dk.yousee.smp.order.model.BusinessException;
import dk.yousee.smp.order.model.CustomerInfo;
import dk.yousee.smp.order.model.Order;
import dk.yousee.smp.order.model.OrderInfo;
import dk.yousee.smp.order.model.OrderStateEnum;
import dk.yousee.smp.order.model.QueryOrdersBySubscriberReply;
import dk.yousee.smp.order.model.Response;
import dk.yousee.smp.order.model.SearchCustomersRequest;
import dk.yousee.smp.order.model.SearchCustomersResponse;
import dk.yousee.smp.smpclient.SmpConnectorImpl;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: aka
 * Date: Oct 14, 2010
 * Time: 10:29:38 PM
 * Testing the subscriber creation
 */
//@Ignore
public class SubscriberCaseIT {
    private static final Logger logger = Logger.getLogger(SubscriberCaseIT.class);

    private SubscriberCase test;
    private SubscriberModel model;
    private Acct acct;
    String orderUrl = null;
    private OrderServiceImpl service = null;

    @Before
    public void setup() {
        acct = new Acct("200000127");  // ronaldo

        SmpConnectorImpl connector=new SmpConnectorImpl();
        connector.setSmpHost(SmpConnectorImpl.T_NET_QA_SMP_HOST);
        connector.setUsername("samp.csra1");
        connector.setPassword("pwcsra1");
//        service.setProxyHost("localhost"); service.setProxyPort("4444");  // used by Anders
//        connector.setProxyHost("sltarray02.tdk.dk"); connector.setProxyPort("8080"); // used by build server
        service = new OrderServiceImpl();
        service.setConnector(connector);

        logger.debug("service allocated");
        Response response;
        response = new Response();
        response.setAcct(acct);
        model = new SubscriberModel(response);
    }

    @Test
    public void testQueryOrdersBySubscriber() {
        QueryOrdersBySubscriberReply reply = service.queryOrdersBySubscriber("601858177");
        if (reply != null) {
            List<OrderInfo> orderInfos = reply.getOrderInfoList();
            for (OrderInfo orderInfo : orderInfos) {
                OrderStateEnum stateEnum = orderInfo.getStateEnum();
                Date date = orderInfo.getOrderDate();
                logger.debug("state="+stateEnum+",date="+date);
            }
            Assert.assertTrue(orderInfos.size()>=10);
        }
    }


    @Test
    public void searchCustomersTest() {
        logger.debug("BssAdapterClient allocated, url=" + orderUrl);
        logger.debug("service allocated");
        SearchCustomersRequest searchCustomersRequest = new SearchCustomersRequest();
        acct = new Acct("603008801");
        searchCustomersRequest.setKundeId(acct);
//        searchCustomersRequest.setCpe_mac("0026f2a98f51");
        SearchCustomersResponse response = service.searchSubscriber(searchCustomersRequest);
        Assert.assertNotNull(response.getXml().getRequest());
        response.getCustomersList();
    }

    @Test
    public void searchCustomerbyVoipPhoneNumber() {
        SearchCustomersRequest searchCustomersRequest = new SearchCustomersRequest();
        searchCustomersRequest.setVoipPhoneNumber("45877497");
        SearchCustomersResponse response = service.searchSubscriber(searchCustomersRequest);
        Assert.assertNotNull(response.getXml().getRequest());
        Assert.assertEquals(1,response.getCustomersList().size());
        CustomerInfo one=response.getCustomersList().get(0);
        Assert.assertEquals("Anders",one.getFirst_name());
    }


    @Test
    public void read_existingSubscription() {
//        bssAdapter = new BssAdapterClient(orderUrl,null);
        logger.debug("BssAdapterClient allocated, url=" + orderUrl);
        logger.debug("service allocated");
//        acct=new Acct("614632161");
        acct = new Acct("616127534");
//        acct=new Acct(" 606092973");
        test = new SubscriberCase(service, acct);
        test.readSubscriber();
        model = test.getModel();
        Assert.assertNotNull(model);
        Assert.assertNotNull(model.getResponse().getSmp());
        Assert.assertNotNull(model.find().SubContactSpec());
        SubContactSpec scs = model.find().SubContactSpec();
        Assert.assertNotNull(scs.first_name);
        logger.debug("first name:" + scs.first_name.getValue());
        Assert.assertNotNull(scs.mittle_name);
        logger.debug("mittle name:" + scs.mittle_name.getValue());
        Assert.assertNotNull(scs.last_name);
        logger.debug("last name:" + scs.last_name.getValue());
        Assert.assertNotNull(model.find().SubAddressSpec());
        int i = 1;
        for (BasicUnit basicUnit : model.getServiceLevelUnit()) {
            logger.info("BASIC UNIT: " + i++);
            logger.info("Type: " + basicUnit.getType());
            logger.info("ExternalKey: " + basicUnit.getExternalKey());
//        	logger.debug("ExternalKey: "+ basicUnit.);
            logger.info("Response Params size: " + basicUnit.getEntity().getParams().size());
            Map<String, String> entityParamsMap = basicUnit.getEntity().getParams();
            for (String paramKey : entityParamsMap.keySet()) {
                logger.info("\tParam Key: " + paramKey);
                logger.info("\tParam Value: " + entityParamsMap.get(paramKey) + "\n");
            }

            for (BasicUnit u2 : basicUnit.getChildrenServices()) {
                logger.info("\tType2: " + u2.getType());
                logger.info("\tExternalKey2: " + u2.getExternalKey());
                logger.info("\tResponse Params size2: " + u2.getEntity().getParams().size());
                Map<String, String> entityParams2 = u2.getEntity().getParams();
                for (String paramKey : entityParams2.keySet()) {
                    logger.info("\t\tParam Key2: " + paramKey);
                    logger.info("\t\tParam Value2: " + entityParams2.get(paramKey) + "\n");
                }
            }
        }
    }

//    @Ignore
    @Test
    public void addSubscription() {
        logger.debug("service allocated");
        acct=new Acct("200000135");
        test = new SubscriberCase(service, acct);
        model = test.getModel();
        if (test.customerExists()) return;
        Assert.assertFalse("customer must not exist", test.customerExists());

        SubscriberCase.CustomerInfo cust = SubscriberCaseTest.fillInCustomerInfo(acct);
        SubscriberCase.AddressInfo addressInfo = SubscriberCaseTest.fillInAddressInfo();
        test.buildAddSubscriptionOrder(cust, addressInfo);
        Order order = test.getModel().getOrder();
        Assert.assertNotNull(order);
        Assert.assertEquals("Expects to send two plans", 2, order.getOrderData().size());
        try {
            test.addSubscription();
        } catch (Throwable e) {
            e.printStackTrace();
            Assert.fail("should not come here:" + e.getMessage());
        }
        Assert.assertTrue("now subscriber is just created",test.isJustCreated());
        SubscriberModel model2 = test.getModel();
        Assert.assertNotNull(model2);
        Assert.assertTrue("should have at least contact and addresss and ", 2 <= model2.getResponse().getSmp().getEntities().size());
    }

    @Ignore
    @Test
    public void updateSubscription() throws Exception {
        logger.debug("service allocated");
        acct = new Acct("200000135");

        test = new SubscriberCase(service, acct);

        SubscriberModel m2 = test.getModel();
        Assert.assertTrue("customer must exist", test.customerExists());

        SubAddressSpec sac = m2.find().SubAddressSpec();
        sac.address1.setValue("Istedgade 2");
        int orderId = test.send();
        Assert.assertNotNull("sending should produce a reply", test.getLastOrderReply());
        Assert.assertTrue(orderId != 0);
        Assert.assertNull("sending should not give error", test.getErrorMessage());
    }

    public static SubscriberCase.AddressInfo fillInAddressInfo() {
        SubscriberCase.AddressInfo addressInfo = new SubscriberCase.AddressInfo();
        addressInfo.setAms("2388237");
        addressInfo.setZipcode("2100");
        addressInfo.setAddress1("Bredgade 27 st.tv");
        addressInfo.setDistrict("PUNE");
        addressInfo.setAddress2("test");
        addressInfo.setCity("test");
        addressInfo.setCountry("Danmark");
        addressInfo.setNtd_return_segment_nm("165");
        return addressInfo;
    }

    @Ignore
    @Test
    public void testUpdateAddress() throws BusinessException {
        acct = new Acct("111222010");
        test = new SubscriberCase(service, acct);
        test.updateAddress(fillInAddressInfo());
        test.send();
    }

    @Ignore
    @Test
    public void updateContact() throws BusinessException {
        acct = new Acct("200000127");
        test = new SubscriberCase(service, acct);
        test.readSubscriber();
        model = test.getModel();
        Assert.assertNotNull(model);
        if(!model.customerExists())return;
        SubscriberCase.CustomerInfo info = test.readContact();
        info.setIsp("1003");
        info.setFirstName("Cristiano");
        info.setLastName("Ronaldo");
        test.updateContact(info);
        Integer send = test.send();
        logger.debug("updateContact send:"+send);
    }

    /**
     * java.lang.ClassCastException: dk.yousee.smp.casemodel.vo.base.SubSpec cannot be cast to dk.yousee.smp.casemodel.vo.base.SubContactSpec
     * at dk.yousee.smp.casemodel.vo.helpers.Find.SubContactSpec(Find.java:58)
     * at dk.yousee.smp.cases.SubscriberCase.deleteSubscription(SubscriberCase.java:306)
     * at dk.yousee.smp.cases.SubscriberCaseIT.deleteSubscription(SubscriberCaseIT.java:121)
     * at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
     * at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:39)
     * at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:25)
     * at org.junit.internal.runners.TestMethod.invoke(TestMethod.java:59)
     * at org.junit.internal.runners.MethodRoadie.runTestMethod(MethodRoadie.java:98)
     * at org.junit.internal.runners.MethodRoadie$2.run(MethodRoadie.java:79)
     * at org.junit.internal.runners.MethodRoadie.runBeforesThenTestThenAfters(MethodRoadie.java:87)
     * at org.junit.internal.runners.MethodRoadie.runTest(MethodRoadie.java:77)
     * at org.junit.internal.runners.MethodRoadie.run(MethodRoadie.java:42)
     * at org.junit.internal.runners.JUnit4ClassRunner.invokeTestMethod(JUnit4ClassRunner.java:88)
     * at org.junit.internal.runners.JUnit4ClassRunner.runMethods(JUnit4ClassRunner.java:51)
     * at org.junit.internal.runners.JUnit4ClassRunner$1.run(JUnit4ClassRunner.java:44)
     * at org.junit.internal.runners.ClassRoadie.runUnprotected(ClassRoadie.java:27)
     * at org.junit.internal.runners.ClassRoadie.runProtected(ClassRoadie.java:37)
     * at org.junit.internal.runners.JUnit4ClassRunner.run(JUnit4ClassRunner.java:42)
     * at org.junit.runner.JUnitCore.run(JUnitCore.java:130)
     * at com.intellij.rt.execution.junit.JUnitStarter.main(JUnitStarter.java:65)
     *
     * @throws Exception on error
     */
    // todo find out why SubSpec is here ???
    @Ignore
    @Test
    public void deleteSubscription() throws Exception {
        test = new SubscriberCase(service, acct);
        Order order = test.deleteSubscription();
        Assert.assertNotNull(order);
        int orderId = test.send();
        Assert.assertTrue(orderId != 0);
        SubscriberModel model2 = test.getModel();
        System.out.println(test.getResponse().getXmlRequest());
        System.out.println(test.getResponse().getXmlResponse());
        Assert.assertNotNull(model2);
    }

}
