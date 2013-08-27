package dk.yousee.smp.cases;

import dk.yousee.smp.casemodel.SubscriberModel;
import dk.yousee.smp.casemodel.vo.BusinessPosition;
import dk.yousee.smp.casemodel.vo.helpers.BasicUnit;
import dk.yousee.smp.functions.OrderServiceImpl;
import dk.yousee.smp.order.model.Acct;
import dk.yousee.smp.order.model.OrderService;
import dk.yousee.smp.smpclient.SmpConnectorImpl;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

@Ignore
public class SikkerhedspakkeCaseIT {
    private static final Logger logger = Logger.getLogger(SikkerhedspakkeCaseIT.class);

    private SikkerhedspakkeCase test;
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
    public void step_04_readAll() throws Exception {
        test = new SikkerhedspakkeCase(orderService, acct);
        model = test.getModel();
        Assert.assertTrue("customer must exist", model.customerExists());
        List<BasicUnit> units = model.filterProgress();
        if (units != null) {
            for (BasicUnit unit : units) {
                Assert.assertTrue(unit.isInProgress());
                Assert.assertNull(unit.getName()); // unit must be a sub service it got no name..
            }
        }

        List<SikkerhedspakkeCase.SikkerhedspakkeActivationData> list = test.readAll();

        for (SikkerhedspakkeCase.SikkerhedspakkeActivationData one : list) {
            Assert.assertNotNull("SikkerhedspakkeActivationData can never be null", one);
            BusinessPosition pos = one.getPosition();
            Assert.assertNotNull("position can never be null", pos);
            String uuid = one.getUuid();
            Assert.assertNotNull("UUID can never be null", uuid);
        }
    }

}
