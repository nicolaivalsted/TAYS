package dk.yousee.smp.casemodel.vo.cbp;

import dk.yousee.smp.casemodel.SubscriberModel;
import dk.yousee.smp.casemodel.vo.ModemId;
import dk.yousee.smp.order.model.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by IntelliJ IDEA.
 * User: aka
 * Date: Oct 23, 2010
 * Time: 12:02:30 PM
 * Tests of the inet access
 */
public class InetAccessTest {

    private SubscriberModel model;
    private CableBBService parent;
    private String externalKey;
    private ModemId modemId;

    @Before
    public void setup() {
        Response response = new Response();
        externalKey = "YouSee:cbb_100510723";
        modemId=ModemId.create("100510723");
        model = new SubscriberModel(response);
        parent = new CableBBService(model, modemId);
    }

    @Test
    public void construction() {
        String externalKey = "yousee:xxx-yyy";
        InetAccess test = new InetAccess(model, externalKey, parent);
        Assert.assertNotNull(test);
        Assert.assertNotNull(test.getType());
        Assert.assertNotNull(parent.getType());
        Assert.assertEquals(externalKey, test.broadband_service_id.getValue());

        test.vrf.setValue("vrf");
        test.vrf_effective.setValue("vrf_effective");
        test.setModemId(modemId);
        test.business_position.setValue("business_position");
        test.activation_reference.setValue("activation_reference");
        test.svc_provider_nm.setValue("test");
        test.upstream_speed.setValue("test");
        test.config_file_override.setValue("test");
        test.bill_ack.setValue("test");
        test.bottom_up_provisioned.setValue("test");
        test.num_of_ips.setValue("test");
        test.gi_address.setValue("test");
        test.aup.setValue("test");
        test.downstream_speed.setValue("test");
        test.email_cos.setValue("test");
    }

    @Test
    public void modemActivationCode() {
        InetAccess test = new InetAccess(model, externalKey, parent);
        String assumedValue = ModemId.extract(externalKey).toString();
        String mAc = test.getModemActivationCode();
        Assert.assertEquals(assumedValue,mAc);
        Assert.assertFalse("No, it is still modemId that is the activation code",test.isModemActivationCodeUsed());

        assumedValue="1234";
        test.modem_activation_code.setValue(assumedValue);
        Assert.assertEquals(assumedValue,test.getModemActivationCode());
        
        Assert.assertTrue("Yes now it is used",test.isModemActivationCodeUsed());
    }

}
