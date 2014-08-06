package dk.yousee.randy.filters.monitoring;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * Stop-gap temporary solution until we can switch to Servlet spec 3.0, where
 * the http status code is readily available.
 * @author jablo
 */
public class ServletResponseWithStatus extends HttpServletResponseWrapper {
    private int httpStatus;

    public ServletResponseWithStatus(HttpServletResponse response) {
        super(response);
    }

    @Override
    public void sendError(int sc) throws IOException {
        httpStatus = sc;
        super.sendError(sc);
    }

    @Override
    public void sendError(int sc, String msg) throws IOException {
        httpStatus = sc;
        super.sendError(sc, msg);
    }

    @Override
    public void setStatus(int sc) {
        httpStatus = sc;
        super.setStatus(sc);
    }

    @Override
    public void setStatus(int sc, String sm) {
        httpStatus = sc;
        super.setStatus(sc, sm);        
    }

    public int getStatus() {
        return httpStatus;
    }
}