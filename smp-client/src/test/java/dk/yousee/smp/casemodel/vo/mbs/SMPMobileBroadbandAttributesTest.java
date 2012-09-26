package dk.yousee.smp.casemodel.vo.mbs;

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
 * Tests of the SMPMobileBroadbandAttributes
 */
public class SMPMobileBroadbandAttributesTest {

    private SubscriberModel model;
    private MobileBBService parent;

    @Before
    public void setup(){
        Response response=new Response();

        model =new SubscriberModel(response);
        parent=new MobileBBService(model,"horse");
    }
    @Test
    public void test_orderTypeParent() {
        String externalKey="yousee:xxx-yyy";
        SMPMobileBroadbandAttributes test=new SMPMobileBroadbandAttributes(model,externalKey,parent);
        Assert.assertNotNull(test);
        Assert.assertNotNull(test.getType());
        Assert.assertNotNull(parent.getType());
        Assert.assertEquals(externalKey,test.mobilebb_service_attribs_id.getValue());
    }
}
