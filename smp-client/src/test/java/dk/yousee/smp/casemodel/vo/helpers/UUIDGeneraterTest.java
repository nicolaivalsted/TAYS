package dk.yousee.smp.casemodel.vo.helpers;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by IntelliJ IDEA.
 * User: m14857
 * Date: Oct 26, 2010
 * Time: 11:38:26 AM
 * To test UUIDGenerater.
 */
public class UUIDGeneraterTest {

    @Test
    public void test_generateKey() {
        String key1 = UUIDGenerater.generateKey();
        String key2 = UUIDGenerater.generateKey();
        System.out.println(key1);
        Assert.assertNotNull(key1);
        Assert.assertTrue(key1.length()>0);
        System.out.println(key2);
        Assert.assertNotNull(key2);
        Assert.assertTrue(key2.length()>0);
        Assert.assertTrue(!key1.equalsIgnoreCase(key2));
    }
}
