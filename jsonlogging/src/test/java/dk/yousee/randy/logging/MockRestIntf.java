/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.yousee.randy.logging;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

public interface MockRestIntf {
    @Path("/my/path/{mac}")
    Response mockRestMethod(@Context UriInfo uriInfo, @PathParam("mac") String pparm, @QueryParam("ip") String qparm, String reqEntity);
}

