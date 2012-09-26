package dk.yousee.smp.casemodel.vo.cpee;

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
 * Tests of the VoipAccess
 */
public class VoipAccessTest {

    private SubscriberModel model;
    private CpeComposedService parent;

    @Before
    public void setup(){
        Response response=new Response();

        model =new SubscriberModel(response);
        parent=new CpeComposedService(model,"horse");
    }
    @Test
    public void construction() {
        String externalKey="yousee:xxx-yyy";
        VoipAccess test=new VoipAccess(model,externalKey,parent);
        Assert.assertNotNull(test);
        Assert.assertNotNull(test.getType());
        Assert.assertNotNull(parent.getType());
        Assert.assertEquals(externalKey,test.mta_service_id.getValue());
    }
}
