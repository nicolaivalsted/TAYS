package dk.yousee.smp.cases;

import dk.yousee.smp.casemodel.SubscriberModel;
import dk.yousee.smp.casemodel.vo.BusinessPosition;
import dk.yousee.smp.casemodel.vo.tdcmail.TdcMail;
import dk.yousee.smp.casemodel.vo.tdcmail.TdcMailService;
import dk.yousee.smp.functions.OrderServiceImpl;
import dk.yousee.smp.order.model.Acct;
import dk.yousee.smp.order.model.BusinessException;
import dk.yousee.smp.order.model.Order;
import dk.yousee.smp.smpclient.SmpConnectorImpl;
import java.util.List;
import junit.framework.Assert;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author m27236
 */
@Ignore
public class TdcMailCaseIT {
    private Logger logger = Logger.getLogger(TdcMailCaseIT.class);
    private SubscriberModel model;
    private Acct acct;
    private OrderServiceImpl service = null;
    private final String businessPos = "1337.1337";
    private final String kpm = "123456789015";

    @Before
    public void setup() {
        acct = new Acct("608252633");
        SmpConnectorImpl connector = new SmpConnectorImpl();
        connector.setSmpHost(SmpConnectorImpl.T_NET_QA_SMP_HOST);
        connector.setUsername("samp.csra1");
        connector.setPassword("pwcsra1");
        connector.setOperationTimeout(5000);
        connector.setConnectionTimeout(5000);
//        service.setProxyHost("localhost"); service.setProxyPort("4444");  // used by Anders
//        connector.setProxyHost("sltarray02.tdk.dk"); connector.setProxyPort("8080"); // used by build server
        service = new OrderServiceImpl();
        service.setConnector(connector);
        logger.debug("service allocated");
    }

    //@Test
    public void readData() {
        TdcMailCase case1 = new TdcMailCase(service, acct);
        List<TdcMailService> res = case1.readAll();
        Assert.assertFalse(res.isEmpty());
    }

    //@Test
    public void createProv() {
        TdcMailCase case1 = new TdcMailCase(service, acct);
        try {
            Order order = case1.createProvisioning(BusinessPosition.create(businessPos), new TdcMailCase.TdcMailData());

            Assert.assertNotNull(order);
            Integer i = case1.send(order);
            Assert.assertNotNull(i);


        } catch (BusinessException be) {
            Assert.fail(be.getMessage());
        }
    }

    //@Test
    public void readProv() {
        TdcMailCase case1 = new TdcMailCase(service, acct);
        try {
            TdcMail mail = case1.readProvisioning(BusinessPosition.create(businessPos));
            Assert.assertNotNull(mail);
        } catch (BusinessException be) {
            Assert.fail(be.getMessage());
        }
    }

    //@Test
    public void createResource() {
        TdcMailCase case1 = new TdcMailCase(service, acct);
        try {
            TdcMailCase.TdcMailData lineItem = new TdcMailCase.TdcMailData();
            lineItem.setKpmNumber(kpm);

            Order order = case1.createResource(BusinessPosition.create(businessPos), lineItem);
            Assert.assertNotNull(order);
            Integer i = case1.send(order);
            Assert.assertNotNull(i);

        } catch (BusinessException be) {
            Assert.fail(be.getMessage());

        }
    }

    //@Test
    public void readResource() {
        TdcMailCase case1 = new TdcMailCase(service, acct);

        List<TdcMailService> services = case1.readAll();

        boolean found = false;
        for (TdcMailService service : services) {
            if (service.getPosition().getId().equals(businessPos)) {
                found = true;
                if (!service.getTdcMailResource().kpm_number.getValue().equals(kpm)) {
                    found = false;
                }
            }
        }

        Assert.assertTrue(found);
    }

    //@Test
    public void deleteTdcMail() {
        TdcMailCase case1 = new TdcMailCase(service, acct);
        try {
           boolean done = case1.delete(BusinessPosition.create(businessPos));
           Assert.assertTrue(done);
           Integer i = case1.send();
           Assert.assertNotNull(i);      
        } catch (BusinessException be) {
            Assert.fail(be.getMessage());

        }
    }
}
