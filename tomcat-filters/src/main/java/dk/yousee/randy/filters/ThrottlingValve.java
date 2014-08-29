package dk.yousee.randy.filters;

import java.io.IOException;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.valves.ValveBase;

/**
 * This tomcat valve maintains per root-context active request counts and throttles or
 * limits the number of executing requests to each root context to a
 * configurable max, returning 503 "Max number of requests, try again later"
 * with a Try-Again: header of 1 minute (60 seconds) if the limit is reached.
 * <p>
 * To deploy: Install into tomcat "/lib" directory, and add filter config
 * section to tomcat's global web.xml in conf/web.xml
 * <p>
 * TODO: Implement a configuration file mechanism&mdash;there's not really flexibility enough in
 * the standard valve parameters.
 *
 * @see ThrottlingFilter
 * @author Jacob Lorensen, YouSee, 2014-08-25
 */
public class ThrottlingValve extends ValveBase {
    private final static Logger log = Logger.getLogger(ThrottlingValve.class.getName());
    private int defaultRequestLimit = 24;
    private final ConcurrentHashMap<String, AtomicInteger> rootContextLimits = new ConcurrentHashMap();

    public ThrottlingValve() {
        throw new RuntimeException("This ThrottlingValve is not implemented YET. Missing a way to configure through tomcat config file or similar");
    }

    public void init(FilterConfig filterConfig) throws ServletException {
        log.info(getClass().getName() + " configuring");
        Enumeration<String> initParameterNames = filterConfig.getInitParameterNames();
        while (initParameterNames.hasMoreElements()) {
            String paramName = initParameterNames.nextElement();
            if (paramName.equals("default-request-limit")) {
                defaultRequestLimit = Integer.parseInt(filterConfig.getInitParameter(paramName));
                log.info("Config default request limit " + defaultRequestLimit);
            } else if (paramName.startsWith("/")) {
                String context = paramName;
                int limit = Integer.parseInt(filterConfig.getInitParameter(paramName));
                rootContextLimits.put(context, new AtomicInteger(limit));
                log.info("Config request limit for " + context + " is " + limit);
            } else {
                log.warning("Unknown parameter name " + paramName);
            }
        }
    }

    public void doFilter(ServletRequest _request, ServletResponse _response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) _request;
        HttpServletResponse resp = (HttpServletResponse) _response;
        String context = req.getContextPath();
        log.info("Check-and-decrement context path " + context);
        AtomicInteger activeRequests = rootContextLimits.get(context);
        if (activeRequests == null) {
            log.info("Automatically configuring default number of requests for " + context + " (" + this.defaultRequestLimit + ")");
            activeRequests = new AtomicInteger(this.defaultRequestLimit);
            AtomicInteger oldV = rootContextLimits.putIfAbsent(context, activeRequests);
            if (oldV != null) { // someone beat us to adding a counter for this root context.
                activeRequests = oldV;
            }
        }
        try {
            int requests = activeRequests.decrementAndGet(); // Get from "pool"
            if (requests < 0) {
                resp.setStatus(503); // return 503 too many requests try again later
                resp.setHeader("Retry-After", "60");
                resp.setContentType("application/json");
                resp.getWriter().print("{ \"message\": \"Too many simultaneous requests try again later\" }");
                return;
            }
            chain.doFilter(req, resp); // We're allowed to run, continue
        } finally {
            activeRequests.incrementAndGet(); // return to "pool"
        }
    }


    @Override
    public void invoke(Request rqst, Response rspns) throws IOException, ServletException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
