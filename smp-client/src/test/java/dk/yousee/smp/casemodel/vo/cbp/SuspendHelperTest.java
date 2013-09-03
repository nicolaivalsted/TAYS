package dk.yousee.smp.casemodel.vo.cbp;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by IntelliJ IDEA.
 * User: aka
 * Date: 06/12/11
 * Time: 07.09
 * Test case for suspend helper
 */
public class SuspendHelperTest {

    @Test
    public void getEnum() {

        SuspendHelper.SuspendReasonAbuse test;
        String value;
        value=null;
        test= SuspendHelper.SuspendReasonAbuse.getEnum(value);
        Assert.assertNull("nothings becomes null",test);

        value="   ";
        test= SuspendHelper.SuspendReasonAbuse.getEnum(value);
        Assert.assertNull("blank becomes null",test);

        test= SuspendHelper.SuspendReasonAbuse.getEnum("TEXT");
        Assert.assertEquals(SuspendHelper.SuspendReasonAbuse.UKENDT,test);

        test= SuspendHelper.SuspendReasonAbuse.getEnum("Abuse");
        Assert.assertEquals(SuspendHelper.SuspendReasonAbuse.ABUSE, test);

        test= SuspendHelper.SuspendReasonAbuse.getEnum("ABUSEWARNING");
        Assert.assertEquals(SuspendHelper.SuspendReasonAbuse.ABUSEWARNING, test);
        
        test= SuspendHelper.SuspendReasonAbuse.getEnum("MODEM");
        Assert.assertEquals(SuspendHelper.SuspendReasonAbuse.MODEM, test);
    }
}
