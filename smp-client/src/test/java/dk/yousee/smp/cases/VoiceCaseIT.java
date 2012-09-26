package dk.yousee.smp.cases;

import dk.yousee.smp.casemodel.SubscriberModel;
import dk.yousee.smp.casemodel.vo.ModemId;
import dk.yousee.smp.casemodel.vo.PhoneNumber;
import dk.yousee.smp.casemodel.vo.cpee.VoipAccess;
import dk.yousee.smp.casemodel.vo.cvp.SwitchFeature;
import dk.yousee.smp.order.client.BssAdapterClient;
import dk.yousee.smp.order.model.Acct;
import dk.yousee.smp.order.model.BusinessException;
import dk.yousee.smp.order.model.OrderService;
import dk.yousee.smp.order.model.Response;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.List;

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
    String orderUrl = null;
    private OrderService service;

    @Before
    public void setup() {
        orderUrl="http://194.239.10.197:41203/bss-adapter2/order.service";
//        orderUrl="http://194.239.10.213:26500/bss-adapter2/order.service";
//        orderUrl = "http://localhost:8080/order.service";
        acct = new Acct("100000003");
        Response response;
        response = new Response();
        response.setAcct(acct);
        SubscriberModel model;
//        model = new SubscriberModel(response);

        BssAdapterClient bssAdapter;
        bssAdapter = new BssAdapterClient(orderUrl, new Proxy(Proxy.Type.HTTP, new InetSocketAddress("sltarray02.tdk.dk", 8080)));
        logger.debug("BssAdapterClient allocated, url=" + orderUrl);
        service = bssAdapter.getOrderService();
        logger.debug("service allocated");
        test = new VoiceCase(acct, service);
        model = test.getModel();
        Assert.assertNotNull(model);
    }

    @Ignore
    @Test
    public void createVoice() throws BusinessException {
        ModemId modemID = ModemId.create("100000004");
        VoiceCase.VoiceData voiceData = new VoiceCase.VoiceData();
        voiceData.setCos_restrict_id("4");
        voiceData.setMta_voice_port("1");
        voiceData.setPrivacy("FULL");
        voiceData.setRate_codes("1401002");
        voiceData.setPhoneNumber(PhoneNumber.create("54674327"));
        voiceData.setCnam("PUNESIGMA");
        test.createVoice(modemID, voiceData);


        List<SwitchFeature> switchFeatures =  test.getModel().find().SwitchFeature(modemID);
        //for test
        test.getModel().alloc().SwitchFeature(modemID, switchFeatures.get(0).getExternalKey());
        test.getModel().find().SwitchFeature(modemID, switchFeatures.get(0).getExternalKey());
        test.send();
    }

    @Ignore
    @Test
    public void createVoiceMail() throws BusinessException {
        test.createVoiceMail(ModemId.create("600195777"),PhoneNumber.create("74674391"));
        test.send();
    }

    @Ignore
    @Test
    public void deleteVoice() throws BusinessException {
        test.deleteVoice(new ModemId("605396311"));
        test.send();
    }

    @Ignore
    @Test
    public void suspendVoice() throws BusinessException {
        test.suspendVoice(new ModemId("605396311"));
        test.send();
    }

    @Ignore
    @Test
    public void resumeVoice() throws BusinessException {
        test.resumeVoice(new ModemId("605396311"));
        test.send();
    }

    @Ignore
    @Test
    public void updateDialToneAccess() throws BusinessException {
        VoiceCase.VoiceData voiceData = new VoiceCase.VoiceData();
        voiceData.setPhoneNumber(PhoneNumber.create("12345678"));
        test.updateDialToneAccess(ModemId.create("100000002"), voiceData);
        test.send();
    }

    @Ignore
    @Test
    public void addAssocInternet_access_has_emta_cmForInetAccess() throws BusinessException {
        VoipAccess voipAccess = test.getModel().find().VoipAccess(new ModemId("100000004"));
        test.addDt_has_accessForDialToneAccess(voipAccess, new ModemId("100000004"));
        test.send();
    }

    @Test
    public void construct() throws Exception {
        SubscriberCase customerCase=new SubscriberCase(service,acct);
        test=new VoiceCase(customerCase);
        Assert.assertNotNull(test);
    }
}
