/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.yousee.randy.logging;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jablo
 */
public class RandyContextLoggingFilterTest {
    public RandyContextLoggingFilterTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    // from: http://stackoverflow.com/questions/11451917/how-do-i-unit-test-a-serlvet-filter-with-junit
    @Test
    public void testSomethingAboutDoFilter() {
//        MyFilter filterUnderTest = new MyFilter();
//        filterUnderTest.setModeService(new MockModeService(ModeService.ONLINE));
//        MockFilterChain mockChain = new MockFilterChain();
//        MockServletRequest req = new MockServletRequest("/maintenance.jsp");
//        MockServletResponse rsp = new MockServletResponse();
//
//        filterUnderTest.doFilter(req, rsp, mockChain);
//
//        assertEquals("/maintenance.jsp", rsp.getLastRedirect());
    }

    /**
     * Test of doFilter method, of class RandyContextLoggingFilter.
     */
    @Test
    public void testDoFilter() throws Exception {
//        System.out.println("doFilter");
//        ServletRequest _request = null;
//        ServletResponse _response = null;
//        FilterChain chain = null;
//        RandyContextLoggingFilter instance = new RandyContextLoggingFilter();
//        instance.doFilter(_request, _response, chain);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }
}