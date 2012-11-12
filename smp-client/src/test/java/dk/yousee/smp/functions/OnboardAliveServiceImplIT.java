package dk.yousee.smp.functions;

import dk.yousee.smp.smpclient.SmpConnectorImpl;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * User: aka
 * Date: 14/02/12
 * Time: 08.12
 * Test onboard alive tester.
 */
public class OnboardAliveServiceImplIT {

    private static final Logger logger = Logger.getLogger(OnboardAliveServiceImplIT.class);
    String proxyHost;
    int proxyPort;
    private OnboardAliveServiceImpl test;

    @Before
    public void before() {

        SmpConnectorImpl connector=new SmpConnectorImpl();
        connector.setSmpHost(SmpConnectorImpl.T_NET_QA_SMP_HOST);
        connector.setUsername("samp.csra1");
        connector.setPassword("pwcsra1");
        proxyHost="sltarray02.tdk.dk"; proxyPort=8080; // used by buildServer
        logger.debug("service allocated");
        test = new OnboardAliveServiceImpl();
        test.setConnector(connector);
    }



    @Test
    public void readWsdl() throws Exception{
        String ping=test.readWsdl();
        Assert.assertNotNull("ping not null",ping);
    }

    @Ignore // servers are too fast.
    @Test
    public void readWsdl_fast() {
        test.getConnector().setConnectionTimeout(1);
        String ping= null;
        try {
            ping = test.readWsdl();
            Assert.fail("should not come here, should timeout");
        } catch (Exception e) {
            logger.debug("timeout with " + e.getMessage());
        }
        Assert.assertNull("ping must be null", ping);
    }
}
