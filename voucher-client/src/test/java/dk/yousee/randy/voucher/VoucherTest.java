package dk.yousee.randy.voucher;

import junit.framework.Assert;
import org.junit.Test;

/**
 * User: aka
 * Date: 07/11/12
 * Time: 14.14
 * Test that voucher is formatted correct
 */
public class VoucherTest {


    @Test
    public void verify() throws Exception {
        Voucher voucher=new Voucher("505468075191");
        Assert.assertNotNull(voucher);
        String verify=voucher.verify();
        Assert.assertNull(verify);
    }
    @Test
    public void verify_bad() throws Exception {
        Voucher voucher=new Voucher("111222333444");
        Assert.assertNotNull(voucher);
        String verify=voucher.verify();
        Assert.assertNotNull(verify);
    }
    @Test
    public void verify_len() throws Exception {
        String base="111222";
        String txt=Verhoeff.generateVerhoeff(base);
        Voucher voucher=new Voucher(base+txt);
        Assert.assertNotNull(voucher);
        String verify=voucher.verify();

        Assert.assertNotNull(verify);
    }

    @Test
    public void create() {
        Voucher voucher=Voucher.create("505468075191");
        Assert.assertNotNull(voucher);
    }

    @Test
    public void equals() {
        Voucher voucher1=Voucher.create("505468075191");
        Voucher voucher2=Voucher.create("505468075191");
        Voucher voucher3=Voucher.create("505468075193");
        Assert.assertTrue(voucher1.equals(voucher2));
        Assert.assertFalse(voucher1.equals(voucher3));
    }
}
