package dk.yousee.smp.casemodel.vo.mbs;

import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: aka
 * Date: Nov 3, 2010
 * Time: 11:59:41 AM
 * Tests that formatting works
 */
public class TimeStampPropHolderTest {

    @Test
    public void test() throws Exception{

        TimeStampPropHolder test=new TimeStampPropHolder(null,"horse");

                     //"yyyyMMddHHmmss"
        String txtDate="20101103160459";
        Date date = TimeStampPropHolder.format.parse(txtDate);
        Assert.assertEquals(txtDate,test.date2string(date));
    }
}
