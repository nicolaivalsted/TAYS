package dk.yousee.randy.filters;

import java.io.IOException;
import java.util.Enumeration;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;
import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;

/**
 * This filter maintains per root-context active request counts and throttles or
 * limits the number of executing requests to each root context to a
 * configurable max, returning 503 "Max number of requests, try again later"
 * with a Try-Again: header of 1 minute (60 seconds) if the limit is reached.
 * <p>
 * To deploy: Install into tomcat "/lib" directory, and add filter config
 * section to tomcat's global web.xml in conf/web.xml
 * <p>
 * To install:
 * <ul>
 * <li>Copy jar file to tomcat/lib
 * <li>Add filter definition to tomcat's global web.xml:
 * <pre>
 *     <filter>
 *       <filter-name>requestThrottlingFilter</filter-name>
 *       <filter-class>dk.yousee.randy.filters.ThrottlingFilter</filter-class>
 *       <init-param>
 *           <param-name>default-request-limit</param-name>
 *           <param-value>8</param-value>
 *       </init-param>
 *       <init-param>
 *           <param-name>/accessnet</param-name>
 *            <param-value>16</param-value>
 *        </init-param>
 *        <init-param>
 *            <param-name>/tays</param-name>
 *            <param-value>24</param-value>
 *        </init-param>
 *        <init-param>
 *            <param-name>/cmts</param-name>
 *            <param-value>16</param-value>
 *        </init-param>
 *    </filter>
 *    <filter-mapping>
 *        <filter-name>requestThrottlingFilter</filter-name>
 *        <url-pattern>/*</url-pattern>
 *    </filter-mapping>
 *</pre>
 *
 * </ul>
 * @see ThrottlingValve
 * @author Jacob Lorensen, YouSee, 2014-08-25
 */
public class ThrottlingFilter implements Filter {
    private final static Logger log = Logger.getLogger(ThrottlingFilter.class.getName());
    private AtomicInteger requestLimit;
    private String thisContext;

    public ThrottlingFilter() {
        log.info("Creating instance of ThrottlingFilter");
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        int defaultRequestLimit = 24;
        thisContext = filterConfig.getServletContext().getContextPath();
        log.info(getClass().getName() + " configuring context is " + thisContext);
        Enumeration<String> initParameterNames = filterConfig.getInitParameterNames();
        while (initParameterNames.hasMoreElements()) {
            String paramName = initParameterNames.nextElement();
            if (paramName.equals("default-request-limit")) {
                defaultRequestLimit = Integer.parseInt(filterConfig.getInitParameter(paramName));
                log.info("Config default request limit " + defaultRequestLimit);
            } else if (paramName.startsWith("/") && paramName.equals(thisContext)) {
                String context = paramName;
                int limit = Integer.parseInt(filterConfig.getInitParameter(paramName));
                requestLimit = new AtomicInteger(limit);
                log.info("Config request limit for " + context + " is " + limit);
            } else {
                log.fine("Ignoring context " + paramName);
            }
        }
        if (requestLimit == null) {
            requestLimit = new AtomicInteger(defaultRequestLimit);
        }
    }

    @Override
    public void doFilter(ServletRequest _request, ServletResponse _response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse resp = (HttpServletResponse) _response;
        AtomicInteger activeRequests = requestLimit;
        int requests = activeRequests.decrementAndGet(); // Get from "pool"
        log.info("Enter: " + thisContext + " " + requests);
        try {
            if (requests < 0) {
                resp.setStatus(503); // return 503 too many requests try again later
                resp.setHeader("Retry-After", "60");
                resp.setContentType("application/json");
                resp.getWriter().print("{ \"message\": \"Too many simultaneous requests try again later\" }");
                return;
            }
            chain.doFilter(_request, resp); // We're allowed to run, continue
        } finally {
            log.info("Leave " + thisContext + " " + activeRequests.incrementAndGet()); // return to "pool"
        }
    }

    @Override
    public void destroy() {
    }
}
