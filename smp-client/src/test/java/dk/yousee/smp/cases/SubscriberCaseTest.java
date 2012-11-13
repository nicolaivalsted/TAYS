package dk.yousee.smp.cases;

import dk.yousee.smp.casemodel.SubscriberModel;
import dk.yousee.smp.order.model.Acct;
import dk.yousee.smp.order.model.Action;
import dk.yousee.smp.order.model.Order;
import dk.yousee.smp.order.model.OrderData;
import dk.yousee.smp.order.model.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * User: aka
 * Date: Oct 14, 2010
 * Time: 10:29:38 PM
 * Testing the subscriber creation
 */
public class SubscriberCaseTest {

    private SubscriberModel model;
    private Acct acct;

    @Before
    public void setup() {
        acct = new Acct("111222009");
        Response response;
        response = new Response();
        response.setAcct(acct);
        model = new SubscriberModel(response);
    }

    @Test
    public void zip4ch() {
        SubscriberCase test = new SubscriberCase(null, model);
        Assert.assertNull(test.zip4ch(null));
        Assert.assertNull(test.zip4ch(" "));
        Assert.assertEquals("0000",test.zip4ch("0"));
        Assert.assertEquals("0900",test.zip4ch("900"));
        Assert.assertEquals("horse",test.zip4ch("horse"));
    }
    
    
    @Test
    public void buildAddSubscriptionOrder() {
        SubscriberCase test = new SubscriberCase(null, model);
        SubscriberCase.CustomerInfo cust = fillInCustomerInfo(acct);
        SubscriberCase.AddressInfo addres = fillInAddressInfo();
        test.buildAddSubscriptionOrder(cust, addres);
        Order order = test.getModel().getOrder();
        Assert.assertNotNull(order);
        OrderData odContact = order.getOrderData().get(0);
        Assert.assertNotNull(odContact);
        Assert.assertEquals(Action.ACTIVATE, odContact.getAction());
        OrderData odAdresse = order.getOrderData().get(1);
        Assert.assertNotNull(odAdresse);
        Assert.assertEquals(Action.ACTIVATE, odAdresse.getAction());

    }

    public static SubscriberCase.AddressInfo fillInAddressInfo() {
        SubscriberCase.AddressInfo addressInfo = new SubscriberCase.AddressInfo();
        addressInfo.setZipcode("2100");
        addressInfo.setAddress1("Bredgade 27 st.tv");
        addressInfo.setDistrict("PUNE");
        addressInfo.setAddress2("test");
        addressInfo.setCity("test");
        addressInfo.setCountry("Danmark");
        addressInfo.setNtd_return_segment_nm("165");
        return addressInfo;
    }

    public static SubscriberCase.CustomerInfo fillInCustomerInfo(Acct acct) {
        SubscriberCase.CustomerInfo cust = new SubscriberCase.CustomerInfo();
        cust.setAcct(acct.toString());
        cust.setFirstName("Lars");
        cust.setLastName("Larsen");
        cust.setMobiltlf("+4511223366");
        cust.setEmail("test@test.dk");
        cust.setArbejdstlf("+4511223344");
        cust.setPrivattlf("+4511223355");
        cust.setIsp("city-bb");
        return cust;
    }


}
