package dk.yousee.smp.com;

import dk.yousee.smp.order.model.Action;
import dk.yousee.smp.order.model.SmpManipulationAction;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by IntelliJ IDEA.
 * User: aka
 * Date: Oct 12, 2010
 * Time: 9:12:10 AM<br/>
 * Test that the factory works
 */
public class SmpManipulationActionFactoryTest {


    private SmpManipulationActionFactory smaf =null;
    @Before
    public void setup(){
        smaf =new SmpManipulationActionFactory();
    }

    @Test
    public void toAction() {
        Assert.assertNotNull(smaf);
        Assert.assertEquals(SmpManipulationAction.ADD,smaf.toAction(Action.ACTIVATE));
        Assert.assertEquals(SmpManipulationAction.DELETE,smaf.toAction(Action.DELETE));
    }

    @Test
    public void toAction_nonexist() {
        Assert.assertNotNull(smaf);
        try {
            SmpManipulationAction res=smaf.toAction(Action.CHANGE_IN_PROGRESS);
            Assert.fail("should not come here, res="+res);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue(e.getMessage().startsWith("Unknown action:"));
        }
    }
}
