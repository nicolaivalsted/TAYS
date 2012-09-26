package dk.yousee.smp.order.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by IntelliJ IDEA.
 * User: aka
 * Date: Oct 13, 2010
 * Time: 8:08:03 PM<br/>
 * Test what happens when adding nulls etc
 */
public class OrderDataTest {

    private OrderData data;

    @Before
    public void setup() {
        data=new OrderData();
    }

    @Test
    public void getParams_not_null() {
        Assert.assertNotNull("should give a params element",data.getParams());
    }

    @Test
    public void getParams_value() {
        data.getParams().put("key","value");
        Assert.assertEquals("must return value","value",data.getParams().get("key"));
    }

    @Test
    public void getParams_value_null() {
        data.getParams().put("key",null);
        Assert.assertNull("must return null",data.getParams().get("key"));
    }
    @Test
    public void getParams_key_null() {
        try {
            data.getParams().put(null,"value");
            data.getParams().get(null);
            Assert.fail("should not come here");
        } catch (NullPointerException e) {
            Assert.assertNotNull("Should give npe",e);
        }
    }
    @Test
    public void getParams_dupliate_assign_test() {
        data.getParams().put("key","value1");
        Assert.assertEquals("must return value","value1",data.getParams().get("key"));
        data.getParams().put("key","value2");
        Assert.assertEquals("must return value","value2",data.getParams().get("key"));
    }

    @Test
    public void getParams_iteration() {
        String[] keys=new String[]{"key1","key2"};
        String[] values=new String[]{"value1","value2"};
        data.getParams().put(keys[0],values[0]);
        data.getParams().put(keys[1],values[1]);
        int count=0;

        for(String key:data.getParams().keySet()) {
            Assert.assertEquals("Keys must be in order",keys[count],key);
            Assert.assertEquals("Values must be expected",values[count],data.getParams().get(key));
            count++;
        }
    }
}
