package dk.yousee.smp.cases;

import dk.yousee.smp.casemodel.vo.ModemId;
import dk.yousee.smp.functions.OrderServiceImpl;
import dk.yousee.smp.order.model.Acct;
import dk.yousee.smp.order.model.BusinessException;
import dk.yousee.smp.smpclient.SmpConnectorImpl;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Created by IntelliJ IDEA.
 * User: m14857
 * Date: Dec 2, 2010
 * Time: 2:41:18 PM
 * To test the Node Split use case
 */
//@Ignore
public class NodeSplitCaseIT {


    private static final Logger logger = Logger.getLogger(NodeSplitCaseIT.class);

//    String orderUrl = null;
    NodeSplitCase nodeSplitCase = null;
    OrderServiceImpl service;

    @Before
    public void setup() {
        SmpConnectorImpl connector=new SmpConnectorImpl();
        connector.setSmpHost(SmpConnectorImpl.T_NET_QA_SMP_HOST);
        connector.setUsername("samp.csra1");
        connector.setPassword("pwcsra1");
        service = new OrderServiceImpl();
        service.setConnector(connector);
//        orderUrl="http://194.239.10.197:41203/bss-adapter2/order.service";
//        orderUrl="http://194.239.10.213:26500/bss-adapter2/order.service";
//        orderUrl = "http://localhost:8080/order.service";
//        BssAdapterClient client = new BssAdapterClient(orderUrl, new Proxy(Proxy.Type.HTTP, new InetSocketAddress("sltarray02.tdk.dk", 8080)));
        nodeSplitCase = new NodeSplitCase(new Acct("100000003"), service);
        logger.info("test start");
    }
    
    @Ignore
    @Test
    public void testUpdateForNodeSplit() throws BusinessException {
        nodeSplitCase.getModel();
        nodeSplitCase.updateForNodeSplit(new ModemId("100000004"),"10.50.0.1","0026f2a98f89");
        nodeSplitCase.send();
    }


}
