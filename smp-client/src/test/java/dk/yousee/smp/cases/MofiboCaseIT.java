package dk.yousee.smp.cases;

import dk.yousee.smp.casemodel.SubscriberModel;
import dk.yousee.smp.casemodel.vo.BusinessPosition;
import dk.yousee.smp.casemodel.vo.mofibo.MofiboService;
import dk.yousee.smp.functions.OrderServiceImpl;
import dk.yousee.smp.order.model.Acct;
import dk.yousee.smp.order.model.BusinessException;
import dk.yousee.smp.order.model.Order;
import dk.yousee.smp.order.model.OrderService;
import dk.yousee.smp.smpclient.SmpConnectorImpl;
import java.util.List;
import junit.framework.Assert;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author simonk
 */
public class MofiboCaseIT {

    private static final Logger logger = Logger.getLogger(MofiboCaseIT.class);

    private MofiboCase test;
    private SubscriberModel model;
    private Acct acct;
    private OrderService orderService = null;

    @Before
    public void setup() {
        acct = new Acct("608252633"); //PROD Chr fuglsang

        SmpConnectorImpl connector = new SmpConnectorImpl();
        connector.setSmpHost(SmpConnectorImpl.T_NET_QA_SMP_HOST);
//        connector.setSmpHost(SmpConnectorImpl.T_NET_SMP_HOST);
        connector.setUsername("samp.csra1");
        connector.setPassword("pwcsra1");

        OrderServiceImpl orderServiceImpl = new OrderServiceImpl();
        orderServiceImpl.setConnector(connector);
        orderService = orderServiceImpl;
        logger.debug("service allocated");
    }

    @Test
    public void test__create_mofibo() throws BusinessException {

        test = new MofiboCase(orderService, acct);

        Order createProvisioning = test.createProvisioning(new BusinessPosition("1337"));
        Integer send = test.send(createProvisioning);
        Assert.assertNotNull("Order numer are null", send);
        Assert.assertTrue("ORderid not cool", send > 0);
    }

    @Test
    public void test_read_smpgui_created_mofibo() {

        test = new MofiboCase(orderService, acct);

        List<MofiboService> mofiboService = test.getModel().find().mofiboService();

        Assert.assertFalse("No mofibo", mofiboService.isEmpty());
    }

    @Test
    public void test_delete_mofibo() throws BusinessException {

        test = new MofiboCase(orderService, acct);

        List<MofiboService> mofiboService = test.getModel().find().mofiboService();

        Assert.assertFalse("No mofibo", mofiboService.isEmpty());

        test.delete(mofiboService.get(0).getPosition());
        Integer send = test.send();

        Assert.assertNotNull("Order numer are null", send);
        Assert.assertTrue("Orderid not cool", send > 0);
        
    }
    
    @Test
    public void test_read_mofibo_no_signal() throws InterruptedException {
        Thread.sleep(1000);
        test = new MofiboCase(orderService, acct);

        List<MofiboService> mofiboService = test.getModel().find().mofiboService();

        Assert.assertTrue("To many mofibo", mofiboService.isEmpty());
    }

}
