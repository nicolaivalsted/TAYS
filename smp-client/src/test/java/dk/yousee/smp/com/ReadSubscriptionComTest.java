package dk.yousee.smp.com;

import dk.yousee.smp.order.model.Acct;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: aka
 * Date: 02/06/12
 * Time: 16.49
 * Testing reading
 */
public class ReadSubscriptionComTest {

    @Test
    public void grepPrimaryKey() {
        Assert.assertNull(ReadSubscriptionCom.Parser.grepPrimaryKey(""));
        String bad_A="rimaryKey>135000</smp:primaryKey>";
        Assert.assertNull(ReadSubscriptionCom.Parser.grepPrimaryKey(bad_A));
        String bad_B="primaryKey>135000";
        Assert.assertNull(ReadSubscriptionCom.Parser.grepPrimaryKey(bad_B));
        String ok_A="      <smp:primaryKey>135000</smp:primaryKey>";
        Assert.assertEquals("135000",ReadSubscriptionCom.Parser.grepPrimaryKey(ok_A));
    }

    @Test
    public void createFindSubscriber() {

        Acct acct=new Acct("123456789");
        String request= new ReadSubscriptionCom().convertRequest(acct);

        Assert.assertNotNull(request);
        Assert.assertTrue(request.contains(acct.toString()));
    }

}
