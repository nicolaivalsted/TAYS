package dk.yousee.smp.cases;

import dk.yousee.smp.casemodel.SubscriberModel;
import dk.yousee.smp.casemodel.vo.ModemId;
import dk.yousee.smp.casemodel.vo.cbp.InetAccess;
import dk.yousee.smp.casemodel.vo.cpee.HsdAccess;
import dk.yousee.smp.functions.OrderServiceImpl;
import dk.yousee.smp.order.model.Acct;
import dk.yousee.smp.smpclient.SmpConnectorImpl;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author m27236
 */
@Ignore
public class CableBBActivationCaseIT {
    
    private static final Logger logger = Logger.getLogger(CableBBActivationCaseIT.class);
    
    private CableBBActivationCase test;
    private SubscriberModel model;
    private Acct acct;
    private OrderServiceImpl service = null;
    private MacAddressCase.HsdAccessData accessData;
    
    @Before
    public void setup() {
        acct=new Acct("300000291");

//    <bean id="orderService" class="dk.yousee.smp.functions.OrderServiceImpl">
//        <property name="sigmaServiceUrlProperty" value="${dk.yousee.provisioning.config.SigmaServiceUrlProperty}"/>
//        <property name="proxyHost" value="${dk.yousee.provisioning.config.ProxyHost}"/>
//        <property name="proxyPort" value="${dk.yousee.provisioning.config.ProxyPort}"/>
//        <property name="username" value="${dk.yousee.provisioning.config.Username}"/>
//        <property name="password" value="${dk.yousee.provisioning.config.Password}"/>
//    </bean>

        SmpConnectorImpl connector=new SmpConnectorImpl();
        String hostName; int port;
        hostName = "194.239.10.197"; port = 41203; //QA
//        hostName="194.239.10.213"; port=26500; //UDV
//        hostName = "localhost"; port = 8010; //simulator 1
        connector.setUrl(String.format("http://%s:%s/SmpXmlOrderApi/xmlorder", hostName, port));
        connector.setUsername("samp.csra1");
        connector.setPassword("pwcsra1");
//        service.setProxyHost("localhost"); service.setProxyPort("4444");  // used by Anders
//        connector.setProxyHost("sltarray02.tdk.dk"); connector.setProxyPort("8080"); // used by build server
        service = new OrderServiceImpl();
        service.setConnector(connector);

        logger.debug("service allocated");
        
        accessData = new MacAddressCase.HsdAccessData();
        accessData.setCm_mac("0026f239a708");
        accessData.setCm_manufacturer("Netgear");
        accessData.setCm_model("CG3000");
        accessData.setCm_serial_number("2BG102UL002CC");
        accessData.setCm_technology("DOCSIS VERSION 1.1");
        accessData.setDocsis_3_capable("Y");
        accessData.setEquipment_type("emta");
        accessData.setGi_address("10.9.0.1");
        accessData.setMax_num_cpe("5");
        accessData.setSvc_provider_nm("YouSee");
        accessData.setWifi_capable("Y");
        
    }
    @Ignore
    @Test
    public void addHsdAccessBB() throws Exception{
        test = new CableBBActivationCase(acct, service);
        
        //find modem
        CableBBCase cableBBCase = new CableBBCase(acct, service);
        model = cableBBCase.getModel();
        Assert.assertTrue("Customer must exist: ", model.customerExists());
        
        ModemId modemId = cableBBCase.firstModem();
        
        
        HsdAccess res = test.addHsdAccess(modemId, accessData);
        
        Assert.assertNotNull(res);
        
        InetAccess ia = test.addAssocInternet_access_has_emta_cmForInetAccess(res, modemId);
        
        Assert.assertNotNull(ia);
        
        Assert.assertNotNull(ia.rate_codes.getValue());
        Assert.assertTrue("rate code cant be empty", !ia.rate_codes.getValue().isEmpty());
        
        Integer order = test.send();
        
        Assert.assertNotNull(order);
  
        
    }
    
    
   
  /*  @Test
    public void getSuspendStatus() throws Exception {
        test=new BBreadCase(acct,service);
        model=test.getModel();
        Assert.assertTrue("customer must exist",model.customerExists());

        ModemId modemId=test.firstModem();
        SuspendStatus status = test.getSuspendStatus(modemId);
        Assert.assertNotNull(status);
    } */
}
