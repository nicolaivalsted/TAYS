package dk.yousee.smp.order.model;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Erling´.
 * User: m26778
 * Date: 10-01-2011
 * Time: 13:29:01
 * Test CHANGE_IN_PROGRESS,
 */
public class ActionTest {

    @Test
    public void actionTest(){
        Action action = Action.CHANGE_IN_PROGRESS;
        Assert.assertEquals(Action.CHANGE_IN_PROGRESS, action);
    }
}
