/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.yousee.randy.logging;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import org.apache.log4j.MDC;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;

/**
 *
 * @author jablo
 */
public class RandyContextLoggingAspectTest {
    List<ContextLoggingSearchItem> si;

    public RandyContextLoggingAspectTest() {
        si = new ArrayList();
        si.add(new ContextLoggingSearchItem("ipaddress", new String[]{"ip", "ipaddress"}));
        si.add(new ContextLoggingSearchItem("macaddress", new String[]{"mac", "macaddress"}));
        si.add(new ContextLoggingSearchItem("modemid", new String[]{"modemid"}));
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

    /**
     * Test of pushLoggingContext method, of class RandyContextLoggingAspect.
     */
    @Test
    public void testPushLoggingContext() throws Exception {
        System.out.println("pushLoggingContext");
        final RandyContextLoggingAspect aspect = new RandyContextLoggingAspect();
        // Mock rest method that "spies" on MDC / NDC
        final Map<String, Object> spy = new HashMap();
        MockRestClass target = new MockRestClass(spy, aspect); // Build an aspect
        AspectJProxyFactory factory = new AspectJProxyFactory(target);
        aspect.setSearchItems(si);
        aspect.setGson(new Gson());
        factory.addAspect(aspect);
        MockRestIntf proxy = factory.getProxy();
        // Build parameters
        MockUriInfo mockUriInfo = new MockUriInfo();
        URI requestUri = new URI("http://hej.me.dig/context/my/path/11:22:33:44:55:66");
        mockUriInfo.setRequestUri(requestUri);
        MultivaluedMap<String, String> pparms = new MultivaluedMapImpl();
        pparms.add("mac", "11:22:33:44:55:66");
        mockUriInfo.setPathParameters(pparms);
        MultivaluedMap<String, String> qparms = new MultivaluedMapImpl();
        qparms.add("ip", "10.20.30.40");
        mockUriInfo.setQueryParameters(qparms);
        JsonObject input = new JsonObject();
        input.addProperty("modemid", "123456789");
        // then you can call methods on proxy, and make assertions about aspect, proxy and target
        MDC.put(aspect.getCalluidJson(), UUID.randomUUID());
        aspect.setLogPayloadHttpStatusMin(0);
        Response mockResp = proxy.mockRestMethod(mockUriInfo, "11:22:33:44:55:66", "10.20.30.40", input.toString());
        // make assertions
        assertNotNull(spy.get(aspect.getCalluidJson()));
        assertTrue(UUID.class.isInstance(spy.get(aspect.getCalluidJson())));
        assertNotNull(mockResp);
        assertEquals(mockResp.getStatus(), Response.Status.OK.getStatusCode());
        assertNotNull(mockResp.getEntity());
        assertEquals(mockResp.getEntity(), "Ok");
        // mocking up this proves difficult :(
//        assertNotNull(spy.get(aspect.getInputJson()));
//        assertEquals(spy.get(aspect.getInputJson()), input);
//        assertNotNull(spy.get(aspect.getRequestUriJson()));
//        assertEquals(requestUri, spy.get(aspect.getRequestUriJson()));

    }
}

class MockUriInfo implements UriInfo {
    private URI requestUri;
    private MultivaluedMap<String, String> queryParameters;
    MultivaluedMap<String, String> pathParameters;

    public void setRequestUri(URI requestUri) {
        this.requestUri = requestUri;
    }

    public void setQueryParameters(MultivaluedMap<String, String> queryParameters) {
        this.queryParameters = queryParameters;
    }

    public void setPathParameters(MultivaluedMap<String, String> pathParameters) {
        this.pathParameters = pathParameters;
    }

    @Override
    public MultivaluedMap<String, String> getPathParameters() {
        return pathParameters;
    }

    @Override
    public MultivaluedMap<String, String> getPathParameters(boolean bln) {
        return pathParameters;
    }

    @Override
    public MultivaluedMap<String, String> getQueryParameters() {
        return queryParameters;
    }

    @Override
    public MultivaluedMap<String, String> getQueryParameters(boolean bln) {
        return queryParameters;
    }

//    
    @Override
    public String getPath() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getPath(boolean bln) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<PathSegment> getPathSegments() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<PathSegment> getPathSegments(boolean bln) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public URI getRequestUri() {
        return requestUri;
    }

    @Override
    public UriBuilder getRequestUriBuilder() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public URI getAbsolutePath() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public UriBuilder getAbsolutePathBuilder() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public URI getBaseUri() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public UriBuilder getBaseUriBuilder() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<String> getMatchedURIs() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<String> getMatchedURIs(boolean bln) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Object> getMatchedResources() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    } 
}