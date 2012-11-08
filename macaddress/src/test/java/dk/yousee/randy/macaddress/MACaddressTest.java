/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.yousee.randy.macaddress;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author JABLO
 */
public class MACaddressTest {

    public MACaddressTest() {
    }

//    @BeforeClass
//    public static void setUpClass() throws Exception {
//    }
//
//    @AfterClass
//    public static void tearDownClass() throws Exception {
//    }
    @Before
    public void setUp() {
    }

//    @After
//    public void tearDown() {
//    }
    @Test
    public void macEqual() {
        MACaddress m1 = MACaddress.parseMACaddress("11:FF:33:44:55:66");
        MACaddress m2 = MACaddress.parseMACaddress("11:ff:33:44:55:66");
        Assert.assertEquals(m1, m2);
    }

    @Test
    public void macHash() {
        MACaddress m1 = MACaddress.parseMACaddress("11:22:33:44:55:66");
        MACaddress m2 = MACaddress.parseMACaddress("11:22:33:44:55:66");
        Assert.assertEquals(m1.hashCode(), m2.hashCode());
    }

    @Test
    public void mtaDNS() {
        MACaddress m1 = new MACaddress("mta-1-6-22-33-44-55-66-77");
        Assert.assertEquals("223344556677", m1.toString(MACaddress.fmtPLAIN));
    }

    @Test
    public void cpeDNS() {
        MACaddress m1 = new MACaddress("x1-6-22-33-44-55-66-77");
        Assert.assertEquals("223344556677", m1.toString(MACaddress.fmtPLAIN));
    }

    @Test
    public void cpeDNSLONG() {
        MACaddress m1 = new MACaddress("x1-6-22-33-44-55-66-77.webspeed.dk");
        Assert.assertEquals("223344556677", m1.toString(MACaddress.fmtPLAIN));
    }

    @Test
    public void addToMac1() {
        MACaddress m1 = new MACaddress("11:00:00:00:00:00");
        MACaddress m2 = m1.addToMac(1);
        Assert.assertEquals(new MACaddress("11:00:00:00:00:01"), m2);
    }

    @Test
    public void addToMac256() {
        MACaddress m1 = new MACaddress("11:00:00:00:00:00");
        MACaddress m2 = m1.addToMac(256);
        Assert.assertEquals(new MACaddress("11:00:00:00:01:00"), m2);
    }

    @Test
    public void addToMacCarry() {
        MACaddress m1 = new MACaddress("00:ff:ff:ff:ff:ff");
        MACaddress m2 = m1.addToMac(1);
        Assert.assertEquals(new MACaddress("01:00:00:00:00:00"), m2);
    }
}