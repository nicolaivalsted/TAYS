package dk.yousee.smp.order.model;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by IntelliJ IDEA.
 * User: aka
 * Date: Oct 28, 2010
 * Time: 11:36:16 AM
 * Tests that the string formatting works correct for the exception, because it
 * is so seldom we get errors so we need to test it in lab.
 */
public class BusinessExceptionTest {

    @Test
    public void construct(){

        try {
            throw new BusinessException("%s %s %s","a","b",new Acct("123"));
        } catch (BusinessException e){
            Assert.assertEquals("a b 123",e.getMessage());
        }

    }
}
