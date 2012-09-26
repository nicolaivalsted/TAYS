package dk.yousee.smp.casemodel.vo.cbp;

import dk.yousee.smp.casemodel.SubscriberModel;
import dk.yousee.smp.casemodel.vo.ModemId;
import dk.yousee.smp.order.model.BusinessException;
import dk.yousee.smp.order.model.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by IntelliJ IDEA.
 * User: aka
 * Date: Oct 23, 2010
 * Time: 12:02:30 PM
 * Tests of the SMPStaticIP
 */
public class SMPStaticIPTest {

    private SubscriberModel model;
    private CableBBService parent;

    @Before
    public void setup(){
        Response response=new Response();

        model =new SubscriberModel(response);
        parent=new CableBBService(model,"horse");
    }
    @Test
    public void construction() {
        String externalKey="yousee:xxx-yyy";
        SMPStaticIP test=new SMPStaticIP(model,externalKey,parent);
        Assert.assertNotNull(test);
        Assert.assertNotNull(test.getType());
        Assert.assertNotNull(parent.getType());
        Assert.assertEquals(externalKey,test.staticip_service_id.getValue());
        test.static_ip_address.setValue("test");
    }

    @Test
    public void static_ip_has_std_cpe() throws BusinessException {
        String externalKey="yousee:xxx-yyy";
        SMPStaticIP test=new SMPStaticIP(model,externalKey,parent);
        StdCpe stdCpe=model.add().StdCpe(new ModemId("yyy"));
        test.static_ip_has_std_cpe.add(stdCpe);
    }
}
