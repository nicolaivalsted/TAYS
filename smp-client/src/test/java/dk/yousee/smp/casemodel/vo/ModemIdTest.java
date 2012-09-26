package dk.yousee.smp.casemodel.vo;

import dk.yousee.smp.casemodel.SubscriberModel;
import dk.yousee.smp.order.model.Acct;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by IntelliJ IDEA.
 * User: aka
 * Date: Nov 1, 2010
 * Time: 10:57:18 AM
 * Testing modem Id.
 */
public class ModemIdTest {

    @Test
    public void extract() {
        SubscriberModel model=new SubscriberModel(new Acct("123456789"));
        ModemId modemId=new ModemId("ABCD");
        String externalKey=model.key().MobileBBService(modemId);
        Assert.assertNotNull(externalKey);
        Assert.assertEquals(modemId,ModemId.extract(externalKey));
    }

    @Test
    public void create() {
        Assert.assertNull("nothing is null",ModemId.create(null));
        Assert.assertNull("blank is null",ModemId.create(" "));
        Assert.assertEquals("123 is 123","123",ModemId.create("123").getId());
    }
    @Test
    public void equals() {
        ModemId a=new ModemId("a");
        ModemId b=null;
        Assert.assertFalse("nothing is false",a.equals(b));
        Object c="123";
        Assert.assertFalse("other class is false",a.equals(c));
        Assert.assertNull("blank is null",ModemId.create(" "));
        Assert.assertEquals("123 is 123", "123", ModemId.create("123").getId());
        Assert.assertFalse("other value is false",a.equals(new ModemId("b")));
        Assert.assertTrue("same value is true",a.equals(new ModemId("a")));
    }

}
