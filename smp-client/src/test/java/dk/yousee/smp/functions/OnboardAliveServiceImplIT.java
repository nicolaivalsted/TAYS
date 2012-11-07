package dk.yousee.smp.functions;

import dk.yousee.smp.smpclient.SmpConnectorImpl;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Created by IntelliJ IDEA.
 * User: aka
 * Date: 14/02/12
 * Time: 08.12
 * Test onboard alive tester.
 */
public class OnboardAliveServiceImplIT {

    private static final Logger logger = Logger.getLogger(OnboardAliveServiceImplIT.class);
    String hostName;
    int port;
    String proxyHost;
    int proxyPort;
    private OnboardAliveServiceImpl test;

    @Before
    public void before() {

        SmpConnectorImpl connector=new SmpConnectorImpl();

        hostName="194.239.10.197"; port=41203; //QA
//        hostName="194.239.10.213"; port=26500; //UDV
        connector.setUrl(String.format("http://%s:%s/SmpXmlOrderApi/xmlorder", hostName, port));
        connector.setUsername("samp.csra1");
        connector.setPassword("pwcsra1");

        proxyHost="sltarray02.tdk.dk"; proxyPort=8080; // used by buildServer
//        proxyHost="localhost"; proxyPort=2222; // used by Anders
//        proxyHost="none"; proxyPort=0; // used by prod
//        connector.setProxyHost(proxyHost);
//        connector.setProxyPort(Integer.toString(proxyPort));
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
