package dk.yousee.smp.casemodel.vo.cvp;

import dk.yousee.smp.casemodel.SubscriberModel;
import dk.yousee.smp.order.model.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by IntelliJ IDEA.
 * User: aka
 * Date: Oct 23, 2010
 * Time: 12:02:30 PM<br/>
 * Tests of the SwitchFeature
 */
public class SwitchFeatureTest {

    private SubscriberModel model;
    private CableVoiceService parent;

    @Before
    public void setup(){
        Response response=new Response();

        model =new SubscriberModel(response);
        parent=new CableVoiceService(model,"horse");
    }
    @Test
    public void construction() {
        String externalKey="yousee:xxx-yyy";
        SwitchFeature test=new SwitchFeature(model,externalKey,parent);
        Assert.assertNotNull(test);
        Assert.assertNotNull(test.getType());
        Assert.assertNotNull(parent.getType());
        Assert.assertEquals(externalKey,test.switch_feature_service_id.getValue());
    }
}