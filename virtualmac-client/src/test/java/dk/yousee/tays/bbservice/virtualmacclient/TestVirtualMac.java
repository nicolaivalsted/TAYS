/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.yousee.tays.bbservice.virtualmacclient;

import dk.yousee.randy.virtualmacclient.VirtualMacAllocationVO;
import dk.yousee.randy.virtualmacclient.VirtualMacClient;
import java.util.List;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author jablo
 */
@Ignore
public class TestVirtualMac {
    VirtualMacClient vMacSvc;

    public TestVirtualMac() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        vMacSvc = new VirtualMacClient("http://localhost:47538/virtual-mac");
    }

    @Test
    public void pingTest() {
        String ok = vMacSvc.alive();
        assertEquals("OK", ok);
    }

    @Test
    public void allocateTest() {
        List<VirtualMacAllocationVO> allocVirtualMac = vMacSvc.allocVirtualMac("123456789", 3);
        assertEquals(3, allocVirtualMac.size());
        for (VirtualMacAllocationVO vma : allocVirtualMac) {
            System.out.println("Allocated: " + vma.getMac() + " for " + vma.getSubscriberAccount());
        }
    }
}
