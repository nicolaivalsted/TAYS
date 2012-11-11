package dk.yousee.smp.cases;

import dk.yousee.smp.casemodel.vo.ModemId;
import dk.yousee.smp.casemodel.vo.cbp.AddnCpe;
import dk.yousee.smp.casemodel.vo.cpee.HsdAccess;
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
 * User: aka
 * Date: Nov 8, 2010
 * Time: 12:00:50 PM
 * test af brug af case.
 */
//@Ignore
public class MacAddressCaseIT {


    private static final Logger logger = Logger.getLogger(MacAddressCaseIT.class);

    MacAddressCase macAddressCase = null;

    @Before
    public void setup() {

        SmpConnectorImpl connector=new SmpConnectorImpl();
        connector.setSmpHost(SmpConnectorImpl.T_NET_QA_SMP_HOST);
        connector.setUsername("samp.csra1");
        connector.setPassword("pwcsra1");
        OrderServiceImpl service;
        service = new OrderServiceImpl();
        service.setConnector(connector);
        macAddressCase = new MacAddressCase(new Acct("100000003"), service);
    }


    @Ignore
    @Test
    public void testAssignCMMacAddressForHsdAccess() throws BusinessException {
        macAddressCase.getModel();
        macAddressCase.assignCMMacAddressForHsdAccess("0026f2a98f75", null, new ModemId("100000004"));
        macAddressCase.send();
    }

    @Ignore
    @Test
    public void testAssignCMMacAddressForInetAccess() throws BusinessException {
        macAddressCase.getModel();
        macAddressCase.assignCMMacAddressForInetAccess("0026f2a98f75", new ModemId("100000004"));
        macAddressCase.send();
    }

    @Ignore
    @Test
    public void testAssignCPEMacAddressForStdCpe() throws BusinessException {
        macAddressCase.getModel();
        macAddressCase.assignCPEMacAddressForStdCpe("0026f2a98f75", new ModemId("100000004"));
        macAddressCase.send();
    }

    @Ignore
    @Test
    public void testAssignCMMacAddressForStdCpe() throws BusinessException {
        macAddressCase.getModel();
        macAddressCase.assignCMMacAddressForStdCpe("0026f2a98f75", new ModemId("100000004"));
        macAddressCase.send();
    }

    @Ignore
    @Test
    public void testUpdateCm_macForAddnCpe() throws BusinessException {
        macAddressCase.getModel();
        macAddressCase.updateCm_macForAddnCpe(new ModemId("100000004"), "51825699-fad9-4302-851c-5b75ecbbff44", "0026f2a98f97");
        macAddressCase.send();
    }

    @Ignore
    @Test
    public void testUpdateCpe_macForAddnCpe() throws BusinessException {
        macAddressCase.getModel();
        macAddressCase.updateCpe_macForAddnCpe(new ModemId("100000004"), "51825699-fad9-4302-851c-5b75ecbbff44", "0026f2a98f97");
        macAddressCase.send();
    }

    @Ignore
    @Test
    public void testAddHsdAccess() throws BusinessException {
        macAddressCase.getModel();
        MacAddressCase.HsdAccessData hsdAccessData = new MacAddressCase.HsdAccessData();
        hsdAccessData.setCm_mac("0026f2a98fbc");
        hsdAccessData.setWifi_capable("y");
        hsdAccessData.setDocsis_3_capable("y");
        hsdAccessData.setEquipment_type("emta");
        hsdAccessData.setSvc_provider_nm("YouSee");
        hsdAccessData.setCm_technology("DOCSIS VERSION 1.1");
        hsdAccessData.setMax_num_cpe("5");
//        hsdAccessData.setService_on_address("primary");
        hsdAccessData.setGi_address("10.50.0.1");
        macAddressCase.addHsdAccess(new ModemId("1000000010"), hsdAccessData);
        macAddressCase.send();
    }

    @Ignore
    @Test
    public void testAddVoipAccess() throws BusinessException {
        macAddressCase.getModel();
        macAddressCase.addVoipAccess(new ModemId("100000004"), "12345678903", "0026f2a98f24");
        macAddressCase.send();
    }

    @Ignore
    @Test
    public void testAddAddnCpe() throws BusinessException {
        macAddressCase.getModel();
        macAddressCase.addAddnCpe(new ModemId("100000004"), "0026f2a98f21", "1401002", "0026f2a98f22");
        macAddressCase.send();
    }

    @Ignore
    @Test
    public void testAddAssocInternet_access_has_emta_cmForInetAccess() throws BusinessException {
        HsdAccess hsdAccess = macAddressCase.getModel().find().HsdAccess(new ModemId("100000004"));
        macAddressCase.addAssocInternet_access_has_emta_cmForInetAccess(hsdAccess, new ModemId("100000004"));
        macAddressCase.send();
    }

    @Ignore
    @Test
    public void deleteAddnCpe() throws BusinessException {
        AddnCpe addnCpe = null;
        if (macAddressCase.getModel().find().AddnCpe(new ModemId("100000004")) != null && macAddressCase.getModel().find().AddnCpe(new ModemId("100000004")).size() > 0)
            addnCpe = macAddressCase.getModel().find().AddnCpe(new ModemId("100000004")).get(0);
        if (addnCpe != null) {
            macAddressCase.deleteAddnCpe(new ModemId("100000004"), addnCpe.getExternalKey());
            macAddressCase.send();
        } else {
            logger.info("There is no AddnCpe to delete.");
        }
    }

}
