package dk.yousee.smp.casemodel.vo.cbp;

import dk.yousee.smp.casemodel.SubscriberModel;
import dk.yousee.smp.order.model.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by IntelliJ IDEA.
 * User: aka
 * Date: Oct 23, 2010
 * Time: 12:02:30 PM
 * Tests of the SMPWiFi
 */
public class SMPWiFiTest {

    private SubscriberModel model;
    private CableBBService parent;

    @Before
    public void setup() {
        Response response = new Response();

        model = new SubscriberModel(response);
        parent = new CableBBService(model, "horse");
    }


    @Test
    public void generators() {
        for (int ii = 0; ii < 10; ii++) {

            System.out.println("runde    :"+ii);
            System.out.println("ssid     >" + SMPWiFi.generateSsid()+"<");
            System.out.println("pwd      >" + SMPWiFi.generatePsk()+"<");
            System.out.println("channel  :" + SMPWiFi.generateChannel(0));
        }
    }


    @Test
    public void construction() {
        String externalKey = "yousee:xxx-yyy";
        SMPWiFi test = new SMPWiFi(model, externalKey, parent);
        Assert.assertNotNull(test);
        Assert.assertNotNull(test.getType());
        Assert.assertNotNull(parent.getType());
        Assert.assertEquals(externalKey, test.wifi_service_id.getValue());
        test.ss_id.setValue("test");
        test.psk.setValue("test");
        test.gw_channel_id.setValue("test");
    }
}
