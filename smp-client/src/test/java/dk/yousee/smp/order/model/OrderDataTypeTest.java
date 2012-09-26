package dk.yousee.smp.order.model;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by IntelliJ IDEA.
 * User: aka
 * Date: Oct 27, 2010
 * Time: 4:18:29 PM<br/>
 * Testing the datatype
 */
public class OrderDataTypeTest {

    @Test
    public void hessian() {
        Assert.assertNotNull(new OrderDataType());
    }
    @Test
    public void construct() {
        OrderDataType res=new OrderDataType(ServicePrefix.SubSvcSpec,"X");
        Assert.assertEquals("SubSvcSpec:X",res.toString());
    }
}
