/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.yousee.randy.logging;

import java.util.Map;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.apache.log4j.MDC;

/**
 *
 * @author jablo
 */
public class MockRestClass implements MockRestIntf {
    private final Map<String, Object> spy;
    private final RandyContextLoggingAspect aspect;

    public MockRestClass(Map<String, Object> spy, RandyContextLoggingAspect aspect) {
        this.spy = spy;
        this.aspect = aspect;
    }

//    @Override
    @Path("/my/path/{mac}")
    public Response mockRestMethod(UriInfo uriInfo,
            @PathParam("mac") String pparm,
            @QueryParam("ip") String qparm, String reqEntity, Object output) {
        spy.put(aspect.getCalluidJson(), MDC.get(aspect.getCalluidJson()));
        spy.put(aspect.getUrlpatternJson(), MDC.get(aspect.getUrlpatternJson()));
        spy.put(aspect.getRequestUriJson(), MDC.get(aspect.getRequestUriJson()));
        spy.put(aspect.getInputJson(), MDC.get(aspect.getInputJson()));
        spy.put(aspect.getOutputJson(), MDC.get(aspect.getOutputJson()));
        spy.put(aspect.getHttpstatusJson(), MDC.get(aspect.getHttpstatusJson()));
        spy.put(aspect.getUncaughtexceptionmsgJson(), MDC.get(aspect.getUncaughtexceptionmsgJson()));
        return Response.status(Response.Status.OK).entity(output).build();
    }
}
