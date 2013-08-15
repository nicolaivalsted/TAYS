package dk.yousee.smp.cases;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import dk.yousee.smp.casemodel.SubscriberModel;
import dk.yousee.smp.casemodel.vo.BusinessPosition;
import dk.yousee.smp.casemodel.vo.ModemId;
import dk.yousee.smp.casemodel.vo.PhoneNumber;
import dk.yousee.smp.casemodel.vo.cpee.VoipAccess;
import dk.yousee.smp.functions.OrderServiceImpl;
import dk.yousee.smp.order.model.Acct;
import dk.yousee.smp.order.model.BusinessException;
import dk.yousee.smp.order.model.Response;
import dk.yousee.smp.smpclient.SmpConnectorImpl;

/**
 * Created by IntelliJ IDEA.
 * User: m14857
 * Date: Nov 2, 2010
 * Time: 2:26:07 PM
 * Integration tests for Voice
 */
@Ignore
public class VoiceCaseIT {

    private static final Logger logger = Logger.getLogger(VoiceCaseIT.class);

    private VoiceCase test;
    private Acct acct;
//    String orderUrl = null;
    private OrderServiceImpl service;

    @Before
    public void setup() {
        acct = new Acct("100000003");
        Response response;
        response = new Response();
        response.setAcct(acct);
        SubscriberModel model;
        SmpConnectorImpl connector=new SmpConnectorImpl();
        connector.setSmpHost(SmpConnectorImpl.T_NET_QA_SMP_HOST);
        connector.setUsername("samp.csra1");
        connector.setPassword("pwcsra1");
        service = new OrderServiceImpl();
        service.setConnector(connector);

        logger.debug("service allocated");
        test = new VoiceCase(acct, service);
        model = test.getModel();
        Assert.assertNotNull(model);
    }

    @Ignore
//    @Test
    public void createVoiceMail() throws BusinessException {
        test.createVoiceMail(BusinessPosition.create("600195777"),PhoneNumber.create("74674391"));
        test.send();
    }

    @Ignore
//    @Test
    public void deleteVoice() throws BusinessException {
        test.deleteVoice(new BusinessPosition("605396311"));
        test.send();
    }

    @Ignore
//    @Test
    public void suspendVoice() throws BusinessException {
        test.suspendVoice(new BusinessPosition("605396311"));
        test.send();
    }

    @Ignore
//    @Test
    public void resumeVoice() throws BusinessException {
        test.resumeVoice(new BusinessPosition("605396311"));
        test.send();
    }

    @Ignore
//    @Test
    public void updateDialToneAccess() throws BusinessException {
        VoiceCase.VoiceData voiceData = new VoiceCase.VoiceData();
        voiceData.setPhoneNumber(PhoneNumber.create("12345678"));
        test.updateDialToneAccess(BusinessPosition.create("100000002"), voiceData);
        test.send();
    }

    @Test
    public void construct() throws Exception {
        SubscriberCase customerCase=new SubscriberCase(service,acct);
        test=new VoiceCase(customerCase);
        Assert.assertNotNull(test);
    }
    
    @Ignore
//    @Test
    public void updateVoiceMailRandyState() throws BusinessException{
        test.updateVoiceMailRandyState(new PhoneNumber("48410002"), "Test");
        Integer order = test.send();
        Assert.assertNotNull(order);
    }
}
