package dk.yousee.smp.smpclient;

import org.apache.http.HttpHost;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: aka
 * Date: 09/04/12
 * Time: 15.12
 * Client test
 */
public class SmpConnectorImplTest {

    private static final Logger logger = Logger.getLogger(SmpConnectorImplTest.class);
    String hostName;
    int port;
    String proxyHost;
    int proxyPort;
    SmpConnectorImpl connector=new SmpConnectorImpl();
//    OrderServiceImpl service;

    @Before
    public void before() {
        hostName="194.239.10.197"; port=41203; //QA
//        hostName="194.239.10.213"; port=26500; //UDV
        connector.setUrl(String.format("http://%s:%s/SmpXmlOrderApi/xmlorder",hostName,port));
        connector.setUsername("samp.csra1");
        connector.setPassword("pwcsra1");

//        proxyHost="sltarray02.tdk.dk"; proxyPort=8080; // used by buildServer
//        proxyHost="localhost"; proxyPort=2222; // used by Anders
//        proxyHost="none"; proxyPort=0; // used by prod
//        connector.setProxyHost(proxyHost); connector.setProxyPort(Integer.toString(proxyPort));
        logger.debug("service allocated");
    }
    @Test
    public void fields(){
        connector.setMaxTotalConnections(100);
        Assert.assertEquals(100,connector.getMaxTotalConnections());

        Integer val=1000;
        connector.setOperationTimeout(val);
        Assert.assertEquals(val,connector.getOperationTimeout());

        String pwd="horse";
        connector.setPassword(pwd);
        Assert.assertEquals(pwd,connector.getPassword());
    }

    @Test
    public void extractHttpHost(){
        SmpConnectorImpl test=connector;
        HttpHost httpHost=test.extractHttpHost();
        Assert.assertEquals(hostName, httpHost.getHostName());
        Assert.assertEquals(port,httpHost.getPort());
    }

    @Test
    public void extractProxy(){
        SmpConnectorImpl test=connector;
        HttpHost proxy=test.extractProxy();
        if("none".equals(proxyHost)){
            Assert.assertNull("none means no proxy",proxy);
        } else {
            Assert.assertEquals(proxyHost,proxy.getHostName());
            Assert.assertEquals(proxyPort,proxy.getPort());
        }
    }

    @Test
    public void extractProxy_none(){
        proxyHost="null"; proxyPort=0; // used by prod
        connector.setProxyHost(proxyHost); connector.setProxyPort(Integer.toString(proxyPort));

        SmpConnectorImpl test=connector;
        HttpHost proxy=test.extractProxy();
        Assert.assertNull("no proxy must return null", proxy);
    }

    @Test
    public void encodeBasic() {
        SmpConnectorImpl test=connector;
        Assert.assertEquals("Basic c2FtcC5jc3JhMTpwd2NzcmEx",test.encodeBasic());
    }

}
