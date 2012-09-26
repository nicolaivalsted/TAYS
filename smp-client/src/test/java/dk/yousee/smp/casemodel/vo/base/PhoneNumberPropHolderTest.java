package dk.yousee.smp.casemodel.vo.base;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by IntelliJ IDEA.
 * User: aka
 * Date: Oct 22, 2010
 * Time: 10:34:29 AM
 * Test that the property works, it is tested that wrong input gives blank
 */
public class PhoneNumberPropHolderTest {

    private PhoneNumberPropHolder holder=new PhoneNumberPropHolder(null,null);


    @Test
    public void filterPhoneNumber_null() {
        Assert.assertEquals("",holder.filterPhoneNumber(null));
    }
    @Test
    public void filterPhoneNumber_8() {
        Assert.assertEquals("22166377",holder.filterPhoneNumber("22166377"));
    }
    @Test
    public void filterPhoneNumber_No_8() {
        Assert.assertEquals("",holder.filterPhoneNumber("122166377"));
    }
    @Test
    public void filterPhoneNumber_plus45() {
        Assert.assertEquals("+4522166377",holder.filterPhoneNumber("+4522166377"));
    }
    @Test
    public void filterPhoneNumber_plus45_long() {
        Assert.assertEquals("",holder.filterPhoneNumber("+45221663772"));
    }
    @Test
    public void filterPhoneNumber_no_plus45() {
        Assert.assertEquals("",holder.filterPhoneNumber("+4222166377"));
    }
}
