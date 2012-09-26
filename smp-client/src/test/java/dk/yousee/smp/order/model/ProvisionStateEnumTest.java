package dk.yousee.smp.order.model;


import org.junit.Assert;
import org.junit.Test;

/**
 * Created by IntelliJ IDEA.
 * User: aka
 * Date: Oct 12, 2010
 * Time: 3:10:25 PM<br/>
 * Make sure the read back works
 */
public class ProvisionStateEnumTest {

    @Test
    public void find() {
        ProvisionStateEnum one= ProvisionStateEnum.find("inactive");
        Assert.assertEquals(one,ProvisionStateEnum.INACTIVE);
        Assert.assertNull("expects none",ProvisionStateEnum.find("horse"));
    }

    @Test
    public void progress() {
        Assert.assertTrue(ProvisionStateEnum.DELETE_IN_PROGRESS.isProgress());
        Assert.assertTrue(ProvisionStateEnum.ADD_IN_PROGRESS.isProgress());
        Assert.assertTrue(ProvisionStateEnum.CHANGE_IN_PROGRESS.isProgress());
        Assert.assertTrue(ProvisionStateEnum.COURTESY_BLOCK_IN_PROGRESS.isProgress());
        Assert.assertTrue(ProvisionStateEnum.OPEN_RUNNING.isProgress());
        Assert.assertFalse(ProvisionStateEnum.COURTESY_BLOCK.isProgress());
    }

}
