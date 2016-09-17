package dk.yousee.smp.order.model;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by IntelliJ IDEA.
 * User: m14857
 * Date: Dec 15, 2010
 * Time: 11:30:58 AM
 * To test Acct trim String
 */
public class AcctTest {

    @Test
    public void construct() {

        Acct acct = new Acct(" 606092973");
        Assert.assertEquals(acct.toString(),"606092973");        
    }

    
    @Test
    public void hessianTest(){
        new Acct();
        
    }
}
