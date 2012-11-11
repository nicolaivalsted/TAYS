package dk.yousee.smp.smpclient;

import dk.yousee.smp.functions.OrderServiceImpl;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by IntelliJ IDEA.
 * User: aka
 * Date: 14/02/12
 * Time: 08.12
 * Test that soap client can run
 */
//@Ignore
public class HttpSoapClientImplIT {

    private static final Logger logger = Logger.getLogger(HttpSoapClientImplIT.class);
//    String hostName;
//    int port;
    String proxyHost;
    int proxyPort;
    OrderServiceImpl service;
    String subscriber;

    @Before
    public void before() {
        SmpConnectorImpl connector=new SmpConnectorImpl();

        connector.setSmpHost(SmpConnectorImpl.T_NET_UDV_SMP_HOST);
        connector.setSmpHost(SmpConnectorImpl.T_NET_QA_SMP_HOST);

        connector.setUsername("samp.csra1");
        connector.setPassword("pwcsra1");

        proxyHost = "sltarray02.tdk.dk";proxyPort = 8080; // used by buildServer
//        proxyHost="localhost"; proxyPort=2222; // used by Anders
//        proxyHost = "none"; proxyPort = 0; // used by prod
//        connector.setProxyHost(proxyHost);
//        connector.setProxyPort(Integer.toString(proxyPort));
        logger.debug("service allocated");
        subscriber="608252633"; // christians account
        service = new OrderServiceImpl();
        service.setConnector(connector);
    }

    @After
    public void after() {
        service.destroy();
    }

    @Test
    public void smpHost() throws Exception {
        Assert.assertEquals(SmpConnectorImpl.T_NET_QA_SMP_HOST,service.getConnector().getSmpHost());
        Assert.assertNotSame(SmpConnectorImpl.T_NET_UDV_SMP_HOST,service.getConnector().getSmpHost());
        Assert.assertNotSame(SmpConnectorImpl.T_NET_SMP_HOST,service.getConnector().getSmpHost());
        Assert.assertNotSame(SmpConnectorImpl.K_QA_SMP_HOST,service.getConnector().getSmpHost());
        Assert.assertNotSame(SmpConnectorImpl.K_SMP_HOST,service.getConnector().getSmpHost());
    }

    @Test
    public void query_with_bad_content() throws Exception {
        HttpSoapClientImpl test;
        test = new HttpSoapClientImpl(service.getConnector());
        try {
            test.executeXml("<hej/>",2000);
            Assert.fail("should never come here ");
        } catch (Exception e) {
            String message = e.getMessage();
            Assert.assertTrue(message.contains("500"));
        }
    }

    /**
     * Dress up to query string
     * @param id subscriber id
     * @return xml that means give me customer data, please...
     */
    private String customerQueryXml(String id) {
        return String.format("<getEntityByKeyRequest cascadeLoading=\"true\" xmlns=\"http://www.sigma-systems.com/schemas/3.1/SmpCBECoreSchema\" \n" +
            "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:com=\"http://java.sun.com/products/oss/xml/Common\" xmlns:smp=\"http://www.sigma-systems.com/schemas/3.1/SmpCommonValues\">\n" +
            "  <entityKeyLst>\n" +
            "    <entityKey xsi:type=\"smp:EntityKeyType\" xmlns:smp=\"http://www.sigma-systems.com/schemas/3.1/SmpCBECoreSchema\">\n" +
            "      <com:type>SubSpec:-</com:type>\n" +
            "      <nameQuery>\n" +
            "        <smp1:queryName xmlns:smp1=\"http://www.sigma-systems.com/schemas/3.1/SmpCommonValues\">get_id_by_parm</smp1:queryName>\n" +
            "        <smp1:inputParamList xmlns:smp1=\"http://www.sigma-systems.com/schemas/3.1/SmpCommonValues\">\n" +
            "          <smp1:param name=\"acct\">%s</smp1:param>\n" +
            "        </smp1:inputParamList>\n" +
            "      </nameQuery>\n" +
            "    </entityKey>\n" +
            "  </entityKeyLst>\n" +
            "</getEntityByKeyRequest>",id);
    }

    @Test
    public void read_existing_customer() throws Exception {
        String xml = customerQueryXml(subscriber);
        HttpSoapClientImpl test;
        test = new HttpSoapClientImpl(service.getConnector());
        String res = test.executeXml(xml,3000);
        Assert.assertNotNull(res);
    }

    @Test
    public void read_customer_bad_user() {
        String xml = customerQueryXml(subscriber);

        try {
            HttpSoapClientImpl test;
            service.getConnector().setUsername("horse");
            test = new HttpSoapClientImpl(service.getConnector());
            test.executeXml(xml,1000);
            Assert.fail("should never come here ");
        } catch (Exception e) {
            String message = e.getMessage();
            Assert.assertTrue(message.contains("Unauthorized"));
        }
    }

    @Test
    public void read_missing_customer() throws Exception {
        String xml = customerQueryXml("666777888");
        HttpSoapClientImpl test;
        test = new HttpSoapClientImpl(service.getConnector());
        String res = test.executeXml(xml,1000);
        Assert.assertNotNull(res);
    }

}
